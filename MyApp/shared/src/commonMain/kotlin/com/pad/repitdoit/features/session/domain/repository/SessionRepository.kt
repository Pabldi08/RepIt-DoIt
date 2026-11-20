package com.pad.repitdoit.features.session.domain.repository

import com.pad.repitdoit.features.session.domain.model.Session
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import kotlinx.coroutines.flow.StateFlow

// Define las operaciones para consultar o gestionar las operaciones de autenticación y sesión.
interface SessionRepository {
    val activeSession: StateFlow<Session?>

    suspend fun createUser(
        userCredentials: UserCredentials
    )

    suspend fun verifyUser(
        userCredentials: UserCredentials
    ): Boolean

    suspend fun login(
        userCredentials: UserCredentials
    ): Session?

    suspend fun logout()
}