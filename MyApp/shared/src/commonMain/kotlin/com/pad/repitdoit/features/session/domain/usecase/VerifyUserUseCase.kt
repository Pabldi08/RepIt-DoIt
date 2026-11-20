package com.pad.repitdoit.features.session.domain.usecase

import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository

// Comprueba las credenciales de un usuario.
class VerifyUserUseCase (
    private val repository: SessionRepository
) {
    suspend operator fun invoke(
        userCredentials: UserCredentials
    ): Boolean {
        return repository.verifyUser(userCredentials)
    }
}