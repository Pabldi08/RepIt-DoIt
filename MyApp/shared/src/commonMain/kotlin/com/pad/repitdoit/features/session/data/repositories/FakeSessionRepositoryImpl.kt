package com.pad.repitdoit.features.session.data.repositories

import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.features.session.domain.model.Session
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSessionRepositoryImpl : SessionRepository {

    private val _activeSession = MutableStateFlow<Session?>(
        Session(
            id = "test-session-001",
            userId = LOCAL_TEST_USER_ID
        )
    )

    override val activeSession: StateFlow<Session?> =
        _activeSession.asStateFlow()

    override suspend fun createUser(
        userCredentials: UserCredentials
    ) {
        throw UnsupportedOperationException()
    }

    override suspend fun verifyUser(
        userCredentials: UserCredentials
    ): Boolean {
        throw UnsupportedOperationException()
    }

    override suspend fun login(
        userCredentials: UserCredentials
    ): Session? {
        throw UnsupportedOperationException()
    }

    override suspend fun logout() {}
}