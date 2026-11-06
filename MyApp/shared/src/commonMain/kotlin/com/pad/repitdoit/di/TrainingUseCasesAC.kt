package com.pad.repitdoit.di

import com.pad.repitdoit.features.training.domain.usecase.AddExerciseToWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.CreateWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetExercisesUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetUsersWorkoutsUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveExerciseFromWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RenameWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.UpdateWorkoutExerciseUseCase

interface TrainingUseCasesAC {
    val getWorkoutUseCase: GetWorkoutUseCase
    val getUsersWorkoutsUseCase: GetUsersWorkoutsUseCase
    val createWorkoutUseCase: CreateWorkoutUseCase
    val getExercisesUseCase: GetExercisesUseCase
    val addExerciseToWorkoutUseCase: AddExerciseToWorkoutUseCase
    val updateWorkoutExerciseUseCase: UpdateWorkoutExerciseUseCase
    val removeExerciseFromWorkoutUseCase: RemoveExerciseFromWorkoutUseCase
    val removeWorkoutUseCase: RemoveWorkoutUseCase
    val renameWorkoutUseCase: RenameWorkoutUseCase
}