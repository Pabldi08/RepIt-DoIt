package com.pad.repitdoit.features.profile.domain.repository

import com.pad.repitdoit.features.profile.domain.model.User

// Define las operaciones para consultar o gestionar los datos del perfil.
interface ProfileRepository {
    suspend fun getProfile(): User
    suspend fun updateUser(user: User)
}
