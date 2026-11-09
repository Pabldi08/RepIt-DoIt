package com.pad.repitdoit.features.achievement.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.achievement.domain.model.Achievement
import com.pad.repitdoit.features.achievement.domain.repository.AchievementRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AchievementsRepositoryImpl (
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : AchievementRepository
{
    override suspend fun getAllowedAchievements(): List<Achievement> = withContext(dispatcher) {
        db.achievementQueries
            .getAllowedAchievementsByUser("test-user-001")
            .executeAsList()
            .map { row ->
                Achievement(
                    id = row.id,
                    name = row.name,
                    desc = row.description.orEmpty(),
                    progress = 0,
                    achivementGoal = 0
                )
            }
    }
}