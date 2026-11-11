package com.pad.repitdoit.features.profile.data.repository

import com.pad.repitdoit.features.profile.domain.model.UserProfile
import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository

class FakeProfileRepositoryImpl : ProfileRepository {
    override suspend fun getProfile() = UserProfile(
        displayName = "Test",
        username = "TestUser"
    )
}
