package com.pad.repitdoit

import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.di.DefaultAppContainer
import com.pad.repitdoit.features.session.domain.model.UserCredentials
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val demoCredentials = UserCredentials(username = "test", displayName = null, password = "test1234")

internal suspend fun AppContainer.loginAsDemoUser() {
    assertEquals(LOCAL_TEST_USER_ID, loginUseCase(demoCredentials)?.userId)
}

internal suspend fun signedInFakeContainer(): AppContainer {
    val container = DefaultAppContainer()
    container.createUserUseCase(demoCredentials)
    assertNotNull(container.loginUseCase(demoCredentials))
    return container
}
