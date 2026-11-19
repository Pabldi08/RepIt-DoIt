package com.pad.repitdoit.di

import com.pad.repitdoit.features.session.domain.usecase.CreateUserUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase
import com.pad.repitdoit.features.profile.domain.usecase.UpdateUserUseCase
import com.pad.repitdoit.features.session.domain.usecase.VerifyUserUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetWorkoutHistoryUseCase

interface ProfileUseCasesAC {
    val getProfileUseCase: GetProfileUseCase
    val updateUserUseCase: UpdateUserUseCase

    val getWorkoutHistoryUseCase: GetWorkoutHistoryUseCase

}