package com.pad.repitdoit.features.session.domain.usecase

import com.pad.repitdoit.features.session.domain.model.Session
import com.pad.repitdoit.features.session.domain.repository.SessionRepository
import kotlinx.coroutines.flow.StateFlow

class GetCurrentSessionUseCase(
    private val repository: SessionRepository
) {
    operator fun invoke(): StateFlow<Session?> {
        return repository.activeSession
    }
}