package com.pad.repitdoit.di

import com.pad.repitdoit.features.catalogue.domain.usecase.GetExercisesByMachineUseCase
import com.pad.repitdoit.features.catalogue.domain.usecase.GetExercisesUseCase

interface CatalogueUseCasesAC {
    val getExercisesUseCase: GetExercisesUseCase
    val getExercisesByMachineUseCase: GetExercisesByMachineUseCase
}
