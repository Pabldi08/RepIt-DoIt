package com.pad.multiplatformapp.di

import com.pad.multiplatformapp.features.home.data.repository.FakeHomeRepository
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository
import com.pad.multiplatformapp.features.home.domain.usecase.GetHomeDailyRoutineUseCase
import com.pad.multiplatformapp.features.home.presentation.HomeViewModel

interface AppContainer {

}

class DefaultAppContainer() : AppContainer {
    private val homeRepository: HomeRepository = FakeHomeRepository()
    private val getHomeDailyRoutineUseCase = GetHomeDailyRoutineUseCase(homeRepository)
    val homeViewModel = HomeViewModel(getHomeDailyRoutineUseCase)
}