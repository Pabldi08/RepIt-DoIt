package com.pad.repitdoit.features.profile.domain.usecase

import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.getProfile()
}
