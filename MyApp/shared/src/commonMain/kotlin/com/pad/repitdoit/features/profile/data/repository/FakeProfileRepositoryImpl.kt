package com.pad.repitdoit.features.profile.data.repository

import com.pad.repitdoit.features.profile.domain.model.User
import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository

class FakeProfileRepositoryImpl : ProfileRepository {

    private var currentUser = User(
        username = "TestUser",
        displayName = "Test"
    )

    override suspend fun getProfile(): User {
        return currentUser
    }

    override suspend fun updateUser(user: User) {
        currentUser = user
    }
}