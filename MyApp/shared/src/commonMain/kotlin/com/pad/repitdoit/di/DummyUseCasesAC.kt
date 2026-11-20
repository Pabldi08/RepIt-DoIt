package com.pad.repitdoit.di

import com.pad.repitdoit.features.dummy.domain.usecase.GetExercisesByMuscleUseCase
import com.pad.repitdoit.features.dummy.domain.usecase.GetMuscleNameByIdUseCase

// Agrupa los casos de uso necesarios para cargar los datos del módulo de ejemplo.
interface DummyUseCasesAC {
    val getExercisesByMuscleUseCase: GetExercisesByMuscleUseCase
    val getMuscleNameByIdUseCase: GetMuscleNameByIdUseCase
}