package com.pad.repitdoit.features.session.data.repositories

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.core.utility.hashPassword
import com.pad.repitdoit.core.utility.matchesPassword
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : SessionRepository {

    override suspend fun createUser(
        userCredentials: UserCredentials
    ): Unit = withContext(dispatcher) {

        val userId = generateId()
        val username = userCredentials.username.trim()

        val displayName = userCredentials.displayName
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: username

        require(username.isNotBlank())
        require(userCredentials.password.isNotBlank())

        val passwordHash = withContext(Dispatchers.Default) {
            hashPassword(userCredentials.password)
        }

        db.transaction {

            db.userQueries.insertUser(
                id = userId,
                username = username,
                display_name = displayName,
                last_synced_at = null
            )

            db.userCredentialQueries.insertUserCredential(
                user_id = userId,
                password_hash = passwordHash
            )
        }
    }

    override suspend fun verifyUser(
        userCredentials: UserCredentials
    ): Boolean = withContext(dispatcher) {

        val user = db.userQueries
            .selectUserByUsername(userCredentials.username.trim())
            .executeAsOneOrNull()
            ?: return@withContext false

        val passwordHash = db.userCredentialQueries
            .selectPasswordHashByUserId(user.id)
            .executeAsOneOrNull()
            ?: return@withContext false

        withContext(Dispatchers.Default) {
            matchesPassword(
                userCredentials.password,
                passwordHash
            )
        }
    }
}