package com.pad.repitdoit.features.profile.domain.repository

import com.pad.repitdoit.features.profile.domain.model.User

interface ProfileRepository {
    suspend fun getProfile(): User
    suspend fun updateUser(user: User)
}
