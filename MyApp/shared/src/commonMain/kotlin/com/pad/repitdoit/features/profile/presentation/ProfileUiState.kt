package com.pad.repitdoit.features.profile.presentation

import com.pad.repitdoit.features.profile.domain.model.User
import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry


data class ProfileUiState(
    val profile: User? = null,
    val workoutHistory: List<WorkoutHistoryEntry> = emptyList(),
    val isHistoryLoading: Boolean = false,
    val historyError: String? = null
)
