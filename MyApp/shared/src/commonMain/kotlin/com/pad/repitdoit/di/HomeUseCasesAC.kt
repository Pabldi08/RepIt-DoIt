package com.pad.repitdoit.di

import com.pad.repitdoit.features.gamification.domain.usecase.GetTotalXpUseCase
import com.pad.repitdoit.features.home.domain.usecase.GetHomeDailyWorkoutsUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase

// Agrupa los casos de uso necesarios para cargar los datos de inicio.
interface HomeUseCasesAC {
    val getHomeDailyWorkoutsUseCase: GetHomeDailyWorkoutsUseCase
    val getTotalXpUseCase: GetTotalXpUseCase
    val getProfileUseCase: GetProfileUseCase
}
