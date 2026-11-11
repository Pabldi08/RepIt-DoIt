package com.pad.repitdoit.features.profile.domain.repository

import com.pad.repitdoit.features.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): UserProfile
}
