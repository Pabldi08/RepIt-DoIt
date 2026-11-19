package com.pad.repitdoit.features.profile.presentation

import com.pad.repitdoit.features.profile.domain.model.User
import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry
import com.pad.repitdoit.features.profile.domain.usecase.DailyWorkoutProgress

data class ProfileUiState(
    val profile: User? = null,
    val totalXp: Long? = null,
    val workoutHistory: List<WorkoutHistoryEntry> = emptyList(),
    val weeklyWorkoutProgress: List<DailyWorkoutProgress> = emptyList(),
    val isHistoryLoading: Boolean = false,
    val historyError: String? = null,
    val isLoggingOut: Boolean = false,
    val error: String? = null
)