package com.pad.repitdoit.features.session.domain.usecase

import com.pad.repitdoit.features.session.domain.model.Session
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository

// Inicia una sesión con las credenciales del usuario.
class LoginUseCase(
    private val repository: SessionRepository
) {
    suspend operator fun invoke(
        userCredentials: UserCredentials
    ): Session? {
        return repository.login(userCredentials)
    }
}