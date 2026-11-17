package com.pad.repitdoit.features.gamification.domain.model

enum class RewardType { KEY, BADGE, COSMETIC, COLLECTIBLE }

data class Reward(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val type: RewardType,
    val assetUrl: String?,
    // Unix milliseconds; null means not owned.
    val obtainedAt: Long?
) {
    val isOwned: Boolean get() = obtainedAt != null
}
