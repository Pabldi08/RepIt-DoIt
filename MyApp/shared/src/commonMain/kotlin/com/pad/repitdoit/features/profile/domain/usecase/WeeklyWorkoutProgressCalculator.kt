package com.pad.repitdoit.features.profile.domain.usecase

import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

data class DailyWorkoutProgress(
    val date: LocalDate,
    val minutes: Float
)

class WeeklyWorkoutProgressCalculator {
    operator fun invoke(
        sessions: List<WorkoutHistoryEntry>,
        today: LocalDate,
        timeZone: TimeZone
    ): List<DailyWorkoutProgress> {
        val sessionsByDate = sessions.groupBy { session ->
            Instant.fromEpochMilliseconds(session.finishedAt)
                .toLocalDateTime(timeZone)
                .date
        }

        return (6 downTo 0).map { daysAgo ->
            val date = today.minus(daysAgo, DateTimeUnit.DAY)
            val totalMillis = sessionsByDate[date].orEmpty().sumOf { session ->
                session.finishedAt - session.startedAt
            }

            DailyWorkoutProgress(
                date = date,
                minutes = totalMillis / 60_000f
            )
        }
    }
}