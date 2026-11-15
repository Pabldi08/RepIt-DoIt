package com.pad.repitdoit.features.session.data.repositories

import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository

class FakeSessionRepositoryImpl : SessionRepository {

    override suspend fun createUser(
        userCredentials: UserCredentials
    ) {
        throw UnsupportedOperationException(
            "Registro no disponible"
        )
    }

    override suspend fun verifyUser(
        userCredentials: UserCredentials
    ): Boolean {
        throw UnsupportedOperationException(
            "Autenticación no disponible"
        )
    }
}