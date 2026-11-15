package com.pad.repitdoit.di

import com.pad.repitdoit.features.session.domain.usecase.CreateUserUseCase
import com.pad.repitdoit.features.session.domain.usecase.VerifyUserUseCase

interface SessionUseCasesAC {
    val createUserUseCase: CreateUserUseCase
    val verifyUserUseCase: VerifyUserUseCase
}