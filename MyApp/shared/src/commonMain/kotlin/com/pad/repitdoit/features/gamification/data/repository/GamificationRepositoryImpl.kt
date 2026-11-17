package com.pad.repitdoit.features.gamification.data.repository

import com.pad.repitdoit.core.localdatabase.AchievementCondition
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.gamification.domain.model.*
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class GamificationRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val userId: String
) : GamificationRepository {
    override suspend fun getTotalXp(): Long = withContext(dispatcher) {
        db.userQueries.selectUserById(userId).executeAsOne().total_xp
    }

    override suspend fun getAllowedAchievements(): List<Achievement> =
        getAchievements().filter { it.availableFrom != null && it.conditions.isNotEmpty() }

    override suspend fun getRewards(): List<Reward> = withContext(dispatcher) {
        db.transactionWithResult { readRewards().filter { it.type != RewardType.KEY } }
    }

    private fun readRewards(): List<Reward> {
        val owned = db.userRewardQueries.selectRewardsByUser(userId).executeAsList()
            .associate { it.reward_id to it.obtained_at }
        return db.rewardQueries.selectAllRewards().executeAsList().map {
            Reward(it.id, it.code, it.name, it.description, RewardType.valueOf(it.reward_type),
                it.asset_url, owned[it.id])
        }
    }

    override suspend fun getAchievements(): List<Achievement> = withContext(dispatcher) {
        val now = Clock.System.now()
        val toTimestamp = now.toEpochMilliseconds()
        db.transactionWithResult {
            val rewardsById = readRewards().associateBy { it.id }
            db.achievementQueries.selectAchievementsForUser(userId).executeAsList().map { row ->
                val conditions = db.achievementConditionQueries
                    .selectConditionsByAchievement(row.id).executeAsList().map { condition ->
                        val progress = row.available_from?.let { availableFrom ->
                            val fromTimestamp = condition.period_days?.let { days ->
                                maxOf(availableFrom, (now - days.days).toEpochMilliseconds())
                            } ?: availableFrom
                            readProgress(condition, fromTimestamp, toTimestamp)
                        }
                        com.pad.repitdoit.features.gamification.domain.model.AchievementCondition(
                            id = condition.id, type = condition.condition_type,
                            comparisonOperator = condition.comparison_operator,
                            targetValue = condition.target_value, exerciseId = condition.exercise_id,
                            workoutId = condition.workout_id, workoutType = condition.workout_type,
                            periodDays = condition.period_days, progress = progress
                        )
                    }
                Achievement(
                    id = row.id, code = row.code, name = row.name,
                    desc = row.description.orEmpty(), iconUrl = row.icon_url,
                    requiredRewardId = row.required_reward_id,
                    conditionLogic = ConditionLogic.valueOf(row.condition_logic),
                    availableFrom = row.available_from, unlockedAt = row.unlocked_at,
                    conditions = conditions,
                    rewards = db.achievementRewardQueries.selectRewardsByAchievement(row.id)
                        .executeAsList().map { rewardsById.getValue(it.reward_id) }
                )
            }
        }
    }

    private fun readProgress(
        condition: AchievementCondition,
        fromTimestamp: Long,
        toTimestamp: Long
    ): Long? {
        if (condition.comparison_operator != ">=" ||
            !condition.target_value.isFinite() || condition.target_value < 0 ||
            (condition.period_days ?: 1L) <= 0 ||
            (condition.condition_type == "WORKOUT_COUNT" && condition.exercise_id != null)
        ) return null

        return when (condition.condition_type) {
            "WORKOUT_COUNT" -> db.workoutSessionQueries.getCompletedWorkoutCount(
                userId = userId,
                fromTimestamp = fromTimestamp,
                toTimestamp = toTimestamp,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            "EXERCISE_SETS" -> db.sessionSetQueries.getCompletedExerciseSetsCount(
                userId = userId,
                fromTimestamp = fromTimestamp,
                toTimestamp = toTimestamp,
                sessionToTimestamp = toTimestamp,
                exerciseId = condition.exercise_id,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            "EXERCISE_REPS" -> db.sessionSetQueries.getCompletedExerciseReps(
                userId = userId,
                fromTimestamp = fromTimestamp,
                toTimestamp = toTimestamp,
                sessionToTimestamp = toTimestamp,
                exerciseId = condition.exercise_id,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            else -> null
        }
    }
}
