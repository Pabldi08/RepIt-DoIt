package com.pad.repitdoit.features.session.domain.model

data class UserCredentials(
    val username: String,
    val displayName: String?,
    val password: String
)