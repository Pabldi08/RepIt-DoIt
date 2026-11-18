package com.pad.repitdoit.features.session.domain.usecase

import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import com.pad.repitdoit.features.session.domain.repository.SessionRepository

class CreateUserUseCase (
    private val repository: SessionRepository
) {
    suspend operator fun invoke(
        userCredentials: UserCredentials
    ) {
        repository.createUser(userCredentials)
    }
}