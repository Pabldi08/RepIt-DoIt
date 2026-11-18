package com.pad.repitdoit.features.dummy.data.repository

import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.dummy.domain.model.Exercise
import com.pad.repitdoit.features.dummy.domain.repository.DummyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DummyRepositoryImpl (
    private val db: RepItDoItDatabase,
    private val dispatcher: CoroutineDispatcher
) : DummyRepository{
    override suspend fun getExerciseByMuscle(
        muscleId: String
    ): List<Exercise> = withContext(dispatcher) {

        val exerciseIds = db.muscleExerciseQueries
            .getExerciseByMuscleId(muscleId)
            .executeAsList()

        exerciseIds.mapNotNull { exerciseId ->

            db.exerciseQueries
                .selectExerciseById(exerciseId)
                .executeAsOneOrNull()
                ?.let { row ->
                    Exercise(
                        id = row.id,
                        name = row.name,
                        desc = row.description.orEmpty(),
                        imgURL = row.image_url.orEmpty(),
                        vidURL = row.video_url.orEmpty()
                    )
                }
        }
    }

    override suspend fun getMuscleNameById(
        muscleId: String
    ): String = withContext(dispatcher) {

        db.muscleQueries
            .getMuscleNameById(muscleId)
            .executeAsOneOrNull()
            .orEmpty()
    }
}