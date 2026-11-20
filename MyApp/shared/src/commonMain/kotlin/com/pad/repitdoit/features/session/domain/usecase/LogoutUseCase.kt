package com.pad.repitdoit.features.session.domain.usecase

import com.pad.repitdoit.features.session.domain.repository.SessionRepository

// Cierra la sesión actual.
class LogoutUseCase(
    private val repository: SessionRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}