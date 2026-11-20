package com.pad.repitdoit.features.gamification.domain.model

// Representa un logro, sus condiciones y su estado.
enum class AchievementStatus { LOCKED, AVAILABLE, ACHIEVED }
enum class ConditionLogic { ALL, ANY }

data class Achievement(
    val id: String,
    val code: String,
    val name: String,
    val desc: String,
    val iconUrl: String?,
    val requiredRewardId: String,
    val conditionLogic: ConditionLogic,
    val availableFrom: Long?,
    val unlockedAt: Long?,
    val conditions: List<AchievementCondition>,
    val rewards: List<Reward>,
    val xpReward: Long = 0
) {
    val status: AchievementStatus get() = when {
        unlockedAt != null -> AchievementStatus.ACHIEVED
        availableFrom != null && conditions.isNotEmpty() -> AchievementStatus.AVAILABLE
        else -> AchievementStatus.LOCKED
    }
    val progress: Long? get() = conditions.singleOrNull()?.progress
    val achievementGoal: Double? get() = conditions.singleOrNull()
        ?.takeIf { it.progress != null }?.targetValue

    // Condition satisfaction does not replace the persistent UserAchievement award.
    val conditionsSatisfied: Boolean? get() {
        if (conditions.isEmpty()) return null
        val results = conditions.map { it.isSatisfied }
        return when (conditionLogic) {
            ConditionLogic.ALL -> when {
                false in results -> false
                null in results -> null
                else -> true
            }
            ConditionLogic.ANY -> when {
                true in results -> true
                null in results -> null
                else -> false
            }
        }
    }
}
