package com.pad.repitdoit.di

import com.pad.repitdoit.features.training.domain.usecase.SelectTrainingPlanUseCase

import com.pad.repitdoit.features.training.domain.usecase.CreateTrainingPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.RenameTrainingPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.DeleteTrainingPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.AddWorkoutToPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveWorkoutFromPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.MoveWorkoutToPlanDayUseCase

import com.pad.repitdoit.features.training.domain.usecase.AddExerciseToWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.CreateWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.CompleteWorkoutSessionUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetUsersWorkoutsUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetUsersTrainingPlansUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetExerciseContextUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveExerciseFromWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RenameWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.UpdateWorkoutExerciseUseCase

// Agrupa los casos de uso necesarios para gestionar los planes y entrenamientos.
interface TrainingUseCasesAC {
    val selectTrainingPlanUseCase: SelectTrainingPlanUseCase
    val createTrainingPlanUseCase: CreateTrainingPlanUseCase
    val renameTrainingPlanUseCase: RenameTrainingPlanUseCase
    val deleteTrainingPlanUseCase: DeleteTrainingPlanUseCase
    val addWorkoutToPlanUseCase: AddWorkoutToPlanUseCase
    val removeWorkoutFromPlanUseCase: RemoveWorkoutFromPlanUseCase
    val moveWorkoutToPlanDayUseCase: MoveWorkoutToPlanDayUseCase
    val getUsersTrainingPlansUseCase: GetUsersTrainingPlansUseCase
    val getExerciseContextUseCase: GetExerciseContextUseCase
    val getWorkoutUseCase: GetWorkoutUseCase
    val getUsersWorkoutsUseCase: GetUsersWorkoutsUseCase
    val createWorkoutUseCase: CreateWorkoutUseCase
    val completeWorkoutSessionUseCase: CompleteWorkoutSessionUseCase
    val addExerciseToWorkoutUseCase: AddExerciseToWorkoutUseCase
    val updateWorkoutExerciseUseCase: UpdateWorkoutExerciseUseCase
    val removeExerciseFromWorkoutUseCase: RemoveExerciseFromWorkoutUseCase
    val removeWorkoutUseCase: RemoveWorkoutUseCase
    val renameWorkoutUseCase: RenameWorkoutUseCase
}
