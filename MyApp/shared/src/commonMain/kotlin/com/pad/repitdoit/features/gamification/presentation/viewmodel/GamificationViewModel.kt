package com.pad.repitdoit.features.gamification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.di.GamificationUseCaseAC
import com.pad.repitdoit.features.gamification.presentation.action.GamificationAction
import com.pad.repitdoit.features.gamification.presentation.state.GamificationState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Carga los datos de progreso, logros y recompensas para la interfaz.
class GamificationViewModel(
    private val appContainer: GamificationUseCaseAC
) : ViewModel() {
    private val _uiState = MutableStateFlow(GamificationState())
    val uiState: StateFlow<GamificationState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                appContainer.awardCompletedAchievementsUseCase()
                coroutineScope {
                    val achievements = async { appContainer.getAchievementsUseCase() }
                    val rewards = async { appContainer.getRewardsUseCase() }
                    val totalXp = async { appContainer.getTotalXpUseCase() }
                    val loadedAchievements = achievements.await()
                    val loadedRewards = rewards.await()
                    val loadedXp = totalXp.await()
                    _uiState.update {
                        it.copy(achievements = loadedAchievements, rewards = loadedRewards,
                            totalXp = loadedXp, isLoading = false)
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message ?: "No se pudo cargar el progreso")
                }
            }
        }
    }

    fun onAction(action: GamificationAction){

    }

}
