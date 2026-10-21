package com.pad.repitdoit.di

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.home.data.repository.FakeHomeRepositoryImpl
import com.pad.repitdoit.features.home.data.repository.HomeRepositoryImpl
import com.pad.repitdoit.features.home.domain.repository.HomeRepository
import com.pad.repitdoit.features.home.domain.usecase.GetHomeDailyWorkoutsUseCase
import com.pad.repitdoit.features.home.presentation.HomeViewModel
import com.pad.repitdoit.features.workout.data.repository.FakeWorkoutRepositoryImpl
import com.pad.repitdoit.features.workout.domain.repository.WorkoutRepository
import com.pad.repitdoit.features.workout.domain.usecase.GetWorkoutUseCase
import com.pad.repitdoit.features.workout.presentation.WorkoutViewModel
import kotlinx.coroutines.CoroutineDispatcher

interface AppContainer {
    val homeViewModel: HomeViewModel
    val workoutViewModel: WorkoutViewModel
}

class DefaultAppContainer(
    val db: RepItDoItDatabase? = null,
    val dispatcher: CoroutineDispatcher? = null
) : AppContainer {

    /*
        REPOSITORIES
     */
    private val homeRepository: HomeRepository =
        if (db == null || dispatcher == null) {
            FakeHomeRepositoryImpl()
        } else {
            HomeRepositoryImpl(db, dispatcher)
        }
    private val workoutRepository: WorkoutRepository = FakeWorkoutRepositoryImpl()
//        if (db == null || dispatcher == null) {
//            FakeWorkoutRepositoryImpl()
//        } else {
//            WorkoutRepositoryImpl(db, dispatcher)
//        }


    /*
        USE CASES
     */
    private val getHomeDailyWorkoutsUseCase = GetHomeDailyWorkoutsUseCase(homeRepository)
    private val getWorkoutUseCase = GetWorkoutUseCase(workoutRepository)


    /*
        ViewModels
     */
    override val homeViewModel = HomeViewModel(getHomeDailyWorkoutsUseCase)
    override val workoutViewModel = WorkoutViewModel(getWorkoutUseCase)

}