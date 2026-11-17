package com.pad.repitdoit.features.profile.data.repository

import com.pad.repitdoit.features.profile.domain.model.User
import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository
import com.pad.repitdoit.features.session.data.repositories.FakeSessionRepositoryImpl

class FakeProfileRepositoryImpl(
    private val sessionRepository: FakeSessionRepositoryImpl? = null
) : ProfileRepository {

    private var currentUser = User(
        username = "TestUser",
        displayName = "Test"
    )

    override suspend fun getProfile(): User {
        return sessionRepository?.getCurrentProfile()
            ?: currentUser
    }

    override suspend fun updateUser(user: User) {
        if (sessionRepository != null) {
            sessionRepository.updateCurrentProfile(user)
        } else {
            currentUser = user
        }
    }
}