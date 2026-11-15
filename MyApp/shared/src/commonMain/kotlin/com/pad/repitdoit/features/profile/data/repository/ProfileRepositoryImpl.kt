
package com.pad.repitdoit.features.profile.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.profile.domain.model.User
import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val currentUserId: () -> String?
) : ProfileRepository {

    override suspend fun getProfile(): User =
        withContext(dispatcher) {

            val userId = checkNotNull(currentUserId()) {
                "No hay ninguna sesión activa"
            }

            val row = db.userQueries
                .selectUserById(userId)
                .executeAsOne()

            User(
                username = row.username,
                displayName = row.display_name
            )
        }

    override suspend fun updateUser(user: User): Unit =
        withContext(dispatcher) {

            val userId = checkNotNull(currentUserId()) {
                "No hay ninguna sesión activa"
            }

            db.userQueries.updateUserProfile(
                username = user.username,
                display_name = user.displayName,
                id = userId
            )

            Unit
        }
}
