
package com.pad.repitdoit.features.session.data.repositories

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.core.utility.hashPassword
import com.pad.repitdoit.core.utility.matchesPassword
import com.pad.repitdoit.features.session.domain.model.Session
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class SessionRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : SessionRepository {

    private val _activeSession = MutableStateFlow<Session?>(null)

    override val activeSession: StateFlow<Session?> =
        _activeSession.asStateFlow()

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

    private suspend fun getVerifiedUserId(
        userCredentials: UserCredentials
    ): String? {

        val credentials = db.transactionWithResult<Pair<String, String>?> {

            val user = db.userQueries
                .selectUserByUsername(userCredentials.username.trim())
                .executeAsOneOrNull()
                ?: return@transactionWithResult null

            val passwordHash = db.userCredentialQueries
                .selectPasswordHashByUserId(user.id)
                .executeAsOneOrNull()
                ?: return@transactionWithResult null

            Pair(user.id, passwordHash)
        } ?: return null

        val (userId, passwordHash) = credentials

        val isValid = withContext(Dispatchers.Default) {
            matchesPassword(
                userCredentials.password,
                passwordHash
            )
        }

        return if (isValid) userId else null
    }

    override suspend fun verifyUser(
        userCredentials: UserCredentials
    ): Boolean = withContext(dispatcher) {
        getVerifiedUserId(userCredentials) != null
    }

    override suspend fun login(
        userCredentials: UserCredentials
    ): Session? = withContext(dispatcher) {

        check(_activeSession.value == null) {
            "Ya existe una sesión activa"
        }

        val userId = getVerifiedUserId(userCredentials)
            ?: return@withContext null

        val session = Session(
            id = generateId(),
            userId = userId
        )

        db.userSessionQueries.insertUserSession(
            id = session.id,
            user_id = session.userId,
            device_id = null,
            created_at = Clock.System.now().toEpochMilliseconds(),
            expires_at = null
        )

        _activeSession.value = session

        session
    }

    override suspend fun logout(): Unit =
        withContext(dispatcher) {

            val session = _activeSession.value
                ?: return@withContext

            db.userSessionQueries.deleteUserSession(
                id = session.id,
                user_id = session.userId
            )

            _activeSession.value = null
        }
}
