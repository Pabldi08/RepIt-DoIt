package com.pad.repitdoit.features.profile.presentation

import com.pad.repitdoit.features.profile.domain.model.User

data class ProfileUiState(
    val profile: User? = null,
    val isLoggingOut: Boolean = false,
    val error: String? = null
)