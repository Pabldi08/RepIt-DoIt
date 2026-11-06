package com.pad.repitdoit.di

import com.pad.repitdoit.features.home.domain.usecase.GetHomeDailyWorkoutsUseCase

interface HomeUseCasesAC {
    val getHomeDailyWorkoutsUseCase: GetHomeDailyWorkoutsUseCase
}