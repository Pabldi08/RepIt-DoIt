package com.pad.repitdoit.features.gamification.data.repository

import com.pad.repitdoit.core.localdatabase.AchievementCondition
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.utility.generateId
import com.pad.repitdoit.features.gamification.domain.model.*
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class GamificationRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val currentUserId: () -> String?
) : GamificationRepository {
    override suspend fun awardCompletedAchievements() = withContext(dispatcher) {
        val userId = currentUserId() ?: return@withContext
        awardCompletedAchievementsInTransaction(userId)
    }

    internal fun awardCompletedAchievementsInTransaction(userId: String) = db.transaction {
        var totalXp = db.userQueries.selectUserById(userId).executeAsOneOrNull()?.total_xp
            ?: return@transaction
        val timestamp = Clock.System.now().toEpochMilliseconds()
        db.rewardQueries.selectRewardByCode("KEY_LEVEL_0").executeAsOneOrNull()?.let { key ->
            grantReward(userId, key.id, timestamp)
        }

        // A key or an XP award can make another achievement eligible in the same transaction.
        while (true) {
            val completed = readAchievements(userId, timestamp).filter {
                it.status == AchievementStatus.AVAILABLE && it.conditionsSatisfied == true
            }
            if (completed.isEmpty()) break

            for (achievement in completed) {
                db.userAchievementQueries.insertUserAchievement(
                    generateId(), userId, achievement.id, timestamp
                )
                achievement.rewards.forEach { grantReward(userId, it.id, timestamp) }
                totalXp += achievement.xpReward.coerceAtMost(Long.MAX_VALUE - totalXp)
            }
            db.userQueries.updateTotalXp(totalXp, userId)
        }
    }

    private fun grantReward(userId: String, rewardId: String, timestamp: Long) {
        db.userRewardQueries.grantUserReward(generateId(), userId, rewardId, timestamp)
    }

    override suspend fun getTotalXp(): Long = withContext(dispatcher) {
        val userId = currentUserId() ?: return@withContext 0L
        db.userQueries.selectUserById(userId).executeAsOne().total_xp
    }

    override suspend fun getAllowedAchievements(): List<Achievement> =
        getAchievements().filter { it.availableFrom != null && it.conditions.isNotEmpty() }

    override suspend fun getRewards(): List<Reward> = withContext(dispatcher) {
        val userId = currentUserId() ?: return@withContext emptyList()
        db.transactionWithResult { readRewards(userId).filter { it.type != RewardType.KEY } }
    }

    private fun readRewards(userId: String): List<Reward> {
        val owned = db.userRewardQueries.selectRewardsByUser(userId).executeAsList()
            .associate { it.reward_id to it.obtained_at }
        return db.rewardQueries.selectAllRewards().executeAsList().map {
            Reward(it.id, it.code, it.name, it.description, RewardType.valueOf(it.reward_type),
                it.asset_url, owned[it.id])
        }
    }

    override suspend fun getAchievements(): List<Achievement> = withContext(dispatcher) {
        val userId = currentUserId() ?: return@withContext emptyList()
        val toTimestamp = Clock.System.now().toEpochMilliseconds()
        db.transactionWithResult { readAchievements(userId, toTimestamp) }
    }

    private fun readAchievements(userId: String, toTimestamp: Long): List<Achievement> {
        val rewardsById = readRewards(userId).associateBy { it.id }
        return db.achievementQueries.selectAchievementsForUser(userId).executeAsList().map { row ->
            val conditions = db.achievementConditionQueries
                .selectConditionsByAchievement(row.id).executeAsList().map { condition ->
                    // Access still requires the key; progress includes the entire history.
                    val progress = row.available_from?.let {
                        readProgress(userId, condition, toTimestamp)
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
                id = row.id, code = row.code, name = row.name, xpReward = row.xp_reward,
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

    private fun readProgress(
        userId: String,
        condition: AchievementCondition,
        toTimestamp: Long
    ): Long? {
        if (condition.comparison_operator != ">=" ||
            !condition.target_value.isFinite() || condition.target_value < 0 ||
            (condition.condition_type == "WORKOUT_COUNT" && condition.exercise_id != null)
        ) return null

        return when (condition.condition_type) {
            "WORKOUT_COUNT" -> db.workoutSessionQueries.getCompletedWorkoutCount(
                userId = userId,
                fromTimestamp = Long.MIN_VALUE,
                toTimestamp = toTimestamp,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            "EXERCISE_SETS" -> db.sessionSetQueries.getCompletedExerciseSetsCount(
                userId = userId,
                fromTimestamp = Long.MIN_VALUE,
                toTimestamp = toTimestamp,
                sessionToTimestamp = toTimestamp,
                exerciseId = condition.exercise_id,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            "EXERCISE_REPS" -> db.sessionSetQueries.getCompletedExerciseReps(
                userId = userId,
                fromTimestamp = Long.MIN_VALUE,
                toTimestamp = toTimestamp,
                sessionToTimestamp = toTimestamp,
                exerciseId = condition.exercise_id,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            "DISTINCT_EXERCISE_COUNT" -> db.sessionExerciseQueries.getDistinctCompletedExerciseCount(
                userId = userId,
                toTimestamp = toTimestamp,
                exerciseId = condition.exercise_id,
                workoutId = condition.workout_id,
                workoutType = condition.workout_type
            ).executeAsOne()
            "WORKOUT_CREATED_COUNT" -> db.workoutQueries.getCreatedWorkoutCount(
                userId, toTimestamp, condition.workout_id, condition.workout_type
            ).executeAsOne().takeIf { condition.exercise_id == null }
            "WORKOUT_EXERCISE_COUNT" -> db.workoutQueries.getMaxWorkoutExerciseCount(
                userId, toTimestamp, condition.workout_id, condition.workout_type
            ).executeAsOne().takeIf { condition.exercise_id == null }
            "USER_LEVEL" -> if (condition.exercise_id == null && condition.workout_id == null &&
                condition.workout_type == null) {
                db.userQueries.selectUserById(userId).executeAsOneOrNull()?.let {
                    calculateLevelProgress(it.total_xp).level.toLong()
                }
            } else null
            else -> null
        }
    }
}
