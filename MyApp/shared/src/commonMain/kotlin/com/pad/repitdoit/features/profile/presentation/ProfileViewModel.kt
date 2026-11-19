package com.pad.repitdoit.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pad.repitdoit.features.gamification.domain.usecase.GetTotalXpUseCase
import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.pad.repitdoit.features.profile.domain.usecase.WeeklyWorkoutProgressCalculator
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class ProfileViewModel(getProfileUseCase: GetProfileUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val getWeeklyWorkoutProgress = WeeklyWorkoutProgressCalculator()

    init {
        viewModelScope.launch {
            val profile = getProfileUseCase()

            _uiState.update {
                it.copy(profile = profile)
            }
        }
    }
}
