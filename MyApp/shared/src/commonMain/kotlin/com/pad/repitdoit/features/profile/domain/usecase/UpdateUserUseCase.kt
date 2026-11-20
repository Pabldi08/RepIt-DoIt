package com.pad.repitdoit.features.profile.domain.usecase

import com.pad.repitdoit.features.profile.domain.model.User
import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository

// Guarda cambios en el perfil del usuario.
class UpdateUserUseCase (
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        user: User
    ) = repository.updateUser(user)
}
