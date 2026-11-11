package com.pad.repitdoit.di

import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.features.catalogue.data.repository.CatalogueRepositoryImpl
import com.pad.repitdoit.features.catalogue.data.repository.FakeCatalogueRepositoryImpl
import com.pad.repitdoit.features.catalogue.domain.repository.CatalogueRepository
import com.pad.repitdoit.features.catalogue.domain.usecase.GetExercisesByMachineUseCase
import com.pad.repitdoit.features.gamification.data.repository.FakeGamificationRepositoryImpl
import com.pad.repitdoit.features.gamification.data.repository.GamificationRepositoryImpl
import com.pad.repitdoit.features.gamification.domain.repository.GamificationRepository
import com.pad.repitdoit.features.gamification.domain.usecase.GetAllowedAchievementsUseCase
import com.pad.repitdoit.features.home.data.repository.FakeHomeRepositoryImpl
import com.pad.repitdoit.features.home.data.repository.HomeRepositoryImpl
import com.pad.repitdoit.features.home.domain.repository.HomeRepository
import com.pad.repitdoit.features.home.domain.usecase.GetHomeDailyWorkoutsUseCase
import com.pad.repitdoit.features.profile.data.repository.FakeProfileRepositoryImpl
import com.pad.repitdoit.features.profile.domain.repository.ProfileRepository
import com.pad.repitdoit.features.profile.domain.usecase.GetProfileUseCase
import com.pad.repitdoit.features.training.data.repository.FakeWorkoutRepositoryImpl
import com.pad.repitdoit.features.training.data.repository.WorkoutRepositoryImpl
import com.pad.repitdoit.features.training.domain.repository.WorkoutRepository
import com.pad.repitdoit.features.training.domain.usecase.CreateWorkoutUseCase
import com.pad.repitdoit.features.catalogue.domain.usecase.GetExercisesUseCase
import com.pad.repitdoit.features.training.domain.usecase.AddExerciseToWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.UpdateWorkoutExerciseUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveExerciseFromWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RenameWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetUsersWorkoutsUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetWorkoutUseCase
import kotlinx.coroutines.CoroutineDispatcher

interface AppContainer : GamificationUseCaseAC, TrainingUseCasesAC, HomeUseCasesAC, CatalogueUseCasesAC {
    val getProfileUseCase: GetProfileUseCase

}


class DefaultAppContainer(
    val db: RepItDoItDatabase? = null,
    val dispatcher: CoroutineDispatcher? = null
) : AppContainer {

    /*
        REPOSITORIES
     */
    private val homeRepository: HomeRepository =
        if (db == null || dispatcher == null) {
            FakeHomeRepositoryImpl()
        } else {
            HomeRepositoryImpl(db, dispatcher)
        }
    private val catalogueRepository: CatalogueRepository =
        if (db == null || dispatcher == null) {
            FakeCatalogueRepositoryImpl()
        } else {
            CatalogueRepositoryImpl(db, dispatcher)
        }

    private val workoutRepository: WorkoutRepository =
        if (db == null || dispatcher == null) {
            FakeWorkoutRepositoryImpl()
        } else {
            WorkoutRepositoryImpl(db, dispatcher)
        }

    private val gamificationRepository: GamificationRepository =
        if (db == null || dispatcher == null) {
            FakeGamificationRepositoryImpl()
        } else {
            GamificationRepositoryImpl(db, dispatcher, LOCAL_TEST_USER_ID)
        }


    private val profileRepository: ProfileRepository = FakeProfileRepositoryImpl()

    /*
        USE CASES
     */
    override val getAllowedAchievementsUseCase = GetAllowedAchievementsUseCase(gamificationRepository)
    override val getHomeDailyWorkoutsUseCase = GetHomeDailyWorkoutsUseCase(homeRepository)
    override val getWorkoutUseCase = GetWorkoutUseCase(workoutRepository)
    override val getUsersWorkoutsUseCase = GetUsersWorkoutsUseCase(workoutRepository)
    override val createWorkoutUseCase = CreateWorkoutUseCase(workoutRepository)
    override val getExercisesUseCase = GetExercisesUseCase(catalogueRepository)
    override val getExercisesByMachineUseCase = GetExercisesByMachineUseCase(catalogueRepository)
    override val addExerciseToWorkoutUseCase = AddExerciseToWorkoutUseCase(workoutRepository)
    override val updateWorkoutExerciseUseCase = UpdateWorkoutExerciseUseCase(workoutRepository)
    override val removeExerciseFromWorkoutUseCase = RemoveExerciseFromWorkoutUseCase(workoutRepository)
    override val removeWorkoutUseCase = RemoveWorkoutUseCase(workoutRepository)
    override val renameWorkoutUseCase = RenameWorkoutUseCase(workoutRepository)
    override val getProfileUseCase = GetProfileUseCase(profileRepository)

}
