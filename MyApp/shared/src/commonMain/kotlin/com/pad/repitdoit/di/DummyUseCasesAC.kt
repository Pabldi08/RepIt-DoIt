package com.pad.repitdoit.di

import com.pad.repitdoit.features.dummy.domain.usecase.GetExercisesByMuscleUseCase
import com.pad.repitdoit.features.dummy.domain.usecase.GetMuscleNameByIdUseCase

interface DummyUseCasesAC {
    val getExercisesByMuscleUseCase: GetExercisesByMuscleUseCase
    val getMuscleNameByIdUseCase: GetMuscleNameByIdUseCase
}