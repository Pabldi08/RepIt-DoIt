package com.pad.repitdoit.features.session.domain.repository

import com.pad.repitdoit.features.session.domain.model.UserCredentials

interface SessionRepository {
    suspend fun createUser(userCredentials: UserCredentials)
    suspend fun verifyUser(userCredentials: UserCredentials): Boolean
}