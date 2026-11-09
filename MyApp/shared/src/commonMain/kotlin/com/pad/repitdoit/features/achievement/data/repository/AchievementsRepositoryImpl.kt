package com.pad.repitdoit.features.achievement.data.repository

import com.pad.repitdoit.core.localdatabase.AchievementCondition
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.achievement.domain.model.Achievement
import com.pad.repitdoit.features.achievement.domain.repository.AchievementRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class AchievementsRepositoryImpl(
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val userId: String
) : AchievementRepository {
    override suspend fun getAllowedAchievements(): List<Achievement> = withContext(dispatcher) {
        val now = Clock.System.now()
        val toTimestamp = now.toEpochMilliseconds()
        db.transactionWithResult {
            db.achievementQueries.getAllowedAchievementsByUser(userId).executeAsList().map { row ->
                val condition = db.achievementConditionQueries
                    .selectConditionsByAchievement(row.id)
                    .executeAsList()
                    .singleOrNull()
                val progress = condition?.let {
                    val fromTimestamp = it.period_days?.let { days ->
                        maxOf(row.available_from, (now - days.days).toEpochMilliseconds())
                    } ?: row.available_from
                    readProgress(it, fromTimestamp, toTimestamp)
                }

                Achievement(
                    id = row.id,
                    name = row.name,
                    desc = row.description.orEmpty(),
                    progress = progress,
                    achivementGoal = condition?.target_value?.takeIf { progress != null }
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
