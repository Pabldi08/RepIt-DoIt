package com.pad.repitdoit.features.gamification.presentation.state

import com.pad.repitdoit.features.gamification.domain.model.calculateLevelProgress
import com.pad.repitdoit.features.gamification.domain.model.Achievement
import com.pad.repitdoit.features.gamification.domain.model.Reward
import com.pad.repitdoit.features.gamification.domain.model.RewardType

// Agrupa los datos de progreso, logros y recompensas que se muestran al usuario.
data class GamificationState(
    val achievements: List<Achievement> = emptyList(),
    val rewards: List<Reward> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalXp: Long = 0
) {
    private val levelProgress = calculateLevelProgress(totalXp)
    val level: Int get() = levelProgress.level
    val currentXp: Long get() = levelProgress.currentXp
    val nextLevelXp: Long get() = levelProgress.nextLevelXp

    val collectibles: List<Reward> get() = rewards.filter {
        it.type == RewardType.COLLECTIBLE
    }
    val achievementCount: Int get() = achievements.count()
    val obtainedAchievementCount: Int get() = achievements.count { it.unlockedAt != null }
    val collectibleCount: Int get() = collectibles.count()
    val ownedCollectibleCount: Int get() = collectibles.count { it.isOwned }
}
