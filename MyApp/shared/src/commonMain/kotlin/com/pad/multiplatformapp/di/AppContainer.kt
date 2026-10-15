package com.pad.multiplatformapp.di

import com.pad.multiplatformapp.features.home.data.repository.FakeHomeRepositoryImpl
import com.pad.multiplatformapp.features.home.domain.repository.HomeRepository
import com.pad.multiplatformapp.features.home.domain.usecase.GetHomeDailyWorkoutUseCase
import com.pad.multiplatformapp.features.home.presentation.HomeViewModel
import com.pad.multiplatformapp.features.workout.data.repository.FakeWorkoutRepositoryImpl
import com.pad.multiplatformapp.features.workout.domain.repository.WorkoutRepository
import com.pad.multiplatformapp.features.workout.domain.usecase.GetWorkoutUseCase
import com.pad.multiplatformapp.features.workout.presentation.WorkoutViewModel

interface AppContainer {

}

class DefaultAppContainer() : AppContainer {

    /*
        REPOSITORIES
     */
    private val homeRepository: HomeRepository = FakeHomeRepositoryImpl()
    private val workoutRepository: WorkoutRepository = FakeWorkoutRepositoryImpl()


    /*
        USE CASES
     */
    private val getHomeDailyWorkoutUseCase = GetHomeDailyWorkoutUseCase(homeRepository)
    private val getWorkoutUseCase = GetWorkoutUseCase(workoutRepository)


    /*
        ViewModels
     */
    val homeViewModel = HomeViewModel(getHomeDailyWorkoutUseCase)
    val workoutViewModel = WorkoutViewModel(getWorkoutUseCase)

}