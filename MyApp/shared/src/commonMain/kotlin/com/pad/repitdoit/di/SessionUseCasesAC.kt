package com.pad.repitdoit.di

import com.pad.repitdoit.features.session.domain.usecase.CreateUserUseCase
import com.pad.repitdoit.features.session.domain.usecase.VerifyUserUseCase
import com.pad.repitdoit.features.session.domain.usecase.LoginUseCase
import com.pad.repitdoit.features.session.domain.usecase.LogoutUseCase
import com.pad.repitdoit.features.session.domain.usecase.GetCurrentSessionUseCase

interface SessionUseCasesAC {

    val createUserUseCase: CreateUserUseCase
    val verifyUserUseCase: VerifyUserUseCase
    val loginUseCase: LoginUseCase
    val logoutUseCase: LogoutUseCase
    val getCurrentSessionUseCase: GetCurrentSessionUseCase
}