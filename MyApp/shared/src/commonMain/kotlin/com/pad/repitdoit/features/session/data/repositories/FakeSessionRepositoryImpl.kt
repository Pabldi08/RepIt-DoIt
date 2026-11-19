package com.pad.repitdoit.features.session.data.repositories

import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.features.session.domain.model.Session
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository
import com.pad.repitdoit.features.profile.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSessionRepositoryImpl : SessionRepository {

    private val users = mutableMapOf<String, UserCredentials>()

    private val _activeSession = MutableStateFlow<Session?>(null)

    override val activeSession: StateFlow<Session?> =
        _activeSession.asStateFlow()

    override suspend fun createUser(
        userCredentials: UserCredentials
    ) {
        val username = userCredentials.username.trim()

        require(username.isNotBlank())
        require(userCredentials.password.isNotBlank())

        require(users.values.none { it.username == username }) {
            "El usuario ya existe"
        }

        val userId = generateId()

        users[userId] = userCredentials.copy(
            username = username,
            displayName = userCredentials.displayName
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?: username
        )
    }

    private fun getVerifiedUserId(
        credentials: UserCredentials
    ): String? {
        return users.entries.firstOrNull { (_, user) ->
            user.username == credentials.username.trim() &&
                    user.password == credentials.password
        }?.key
    }

    override suspend fun verifyUser(
        userCredentials: UserCredentials
    ): Boolean {
        return getVerifiedUserId(userCredentials) != null
    }

    override suspend fun login(
        userCredentials: UserCredentials
    ): Session? {

        check(_activeSession.value == null)

        val userId = getVerifiedUserId(userCredentials)
            ?: return null

        val session = Session(
            id = generateId(),
            userId = userId
        )

        _activeSession.value = session

        return session
    }

    override suspend fun logout() {
        _activeSession.value = null
    }

    internal fun getCurrentProfile(): User {
        val userId = checkNotNull(_activeSession.value?.userId)
        val user = users.getValue(userId)

        return User(
            username = user.username,
            displayName = user.displayName ?: user.username
        )
    }

    internal fun updateCurrentProfile(user: User) {
        val userId = checkNotNull(_activeSession.value?.userId)
        val credentials = users.getValue(userId)

        val username = user.username.trim()

        require(username.isNotBlank())
        require(user.displayName.isNotBlank())

        require(users.none { (id, account) ->
            id != userId && account.username == username
        })

        users[userId] = credentials.copy(
            username = username,
            displayName = user.displayName.trim()
        )
    }
}