package com.pad.repitdoit.di

import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase
import com.pad.repitdoit.features.profile.domain.usecase.UpdateUserUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetWorkoutHistoryUseCase

// Agrupa los casos de uso necesarios para consultar y actualizar el perfil.
interface ProfileUseCasesAC {
    val getProfileUseCase: GetProfileUseCase
    val updateUserUseCase: UpdateUserUseCase

    val getWorkoutHistoryUseCase: GetWorkoutHistoryUseCase

}