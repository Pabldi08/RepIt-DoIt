package com.pad.repitdoit.di

import com.pad.repitdoit.features.catalogue.domain.usecase.GetExercisesByMachineUseCase
import com.pad.repitdoit.features.catalogue.domain.usecase.GetExercisesUseCase

// Agrupa los casos de uso necesarios para consultar el catálogo.
interface CatalogueUseCasesAC {
    val getExercisesUseCase: GetExercisesUseCase
    val getExercisesByMachineUseCase: GetExercisesByMachineUseCase
}
