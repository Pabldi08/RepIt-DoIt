package com.pad.repitdoit.features.catalogue.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.di.CatalogueUseCasesAC
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogueViewModel(
    private val appContainer: CatalogueUseCasesAC,
    private val machineId: String? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogueState())
    val uiState: StateFlow<CatalogueState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    fun onAction(action: CatalogueAction) {
        when (action) {
            is CatalogueAction.ChangeQuery -> _uiState.update { it.copy(query = action.query) }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            try {
                val exercises = if (machineId == null) {
                    appContainer.getExercisesUseCase()
                } else {
                    appContainer.getExercisesByMachineUseCase(machineId)
                }

                _uiState.update {
                    it.copy(
                        exercises = exercises,
                        errorMessage = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "No se pudo cargar el listado de ejercicios"
                    )
                }
            }
        }
    }
}
