package com.pad.repitdoit.features.workout.domain.model

data class ScanTarget(
    val id: String,
    val equipmentId: String,
    val qrPayload: String? = null,
    val nfcIdentifier: String? = null,
    val isActive: Boolean
)
