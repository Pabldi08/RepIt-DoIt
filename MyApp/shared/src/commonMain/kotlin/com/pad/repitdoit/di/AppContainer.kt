package com.pad.repitdoit.di

import com.pad.repitdoit.features.training.domain.usecase.SelectTrainingPlanUseCase

import com.pad.repitdoit.features.training.domain.usecase.CreateTrainingPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.RenameTrainingPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.DeleteTrainingPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.AddWorkoutToPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveWorkoutFromPlanUseCase
import com.pad.repitdoit.features.training.domain.usecase.MoveWorkoutToPlanDayUseCase

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
import com.pad.repitdoit.features.profile.data.repository.ProfileRepositoryImpl
import com.pad.repitdoit.features.session.domain.usecase.CreateUserUseCase
import com.pad.repitdoit.features.profile.domain.usecase.UpdateUserUseCase
import com.pad.repitdoit.features.session.data.repositories.FakeSessionRepositoryImpl
import com.pad.repitdoit.features.session.data.repositories.SessionRepositoryImpl
import com.pad.repitdoit.features.session.domain.repository.SessionRepository
import com.pad.repitdoit.features.session.domain.usecase.GetCurrentSessionUseCase
import com.pad.repitdoit.features.session.domain.usecase.LoginUseCase
import com.pad.repitdoit.features.session.domain.usecase.LogoutUseCase
import com.pad.repitdoit.features.session.domain.usecase.VerifyUserUseCase
import com.pad.repitdoit.features.dummy.data.repository.FakeDummyRepositoryImpl
import com.pad.repitdoit.features.dummy.domain.repository.DummyRepository
import com.pad.repitdoit.features.dummy.domain.usecase.GetMuscleNameByIdUseCase
import com.pad.repitdoit.features.training.domain.usecase.AddExerciseToWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.UpdateWorkoutExerciseUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveExerciseFromWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RemoveWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.RenameWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetUsersWorkoutsUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetUsersTrainingPlansUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetWorkoutUseCase
import com.pad.repitdoit.features.training.domain.usecase.GetExerciseContextUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.AwardCompletedAchievementsUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.GetAchievementsUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.GetRewardsUseCase
import com.pad.repitdoit.features.gamification.domain.usecase.GetTotalXpUseCase
import com.pad.repitdoit.features.training.domain.usecase.CompleteWorkoutSessionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import com.pad.repitdoit.features.dummy.data.repository.DummyRepositoryImpl
import com.pad.repitdoit.features.dummy.domain.model.Exercise
import com.pad.repitdoit.features.dummy.domain.usecase.GetExercisesByMuscleUseCase
import com.pad.repitdoit.features.profile.data.repository.WorkoutHistoryRepositoryImpl
import com.pad.repitdoit.features.profile.domain.model.WorkoutHistoryEntry
import com.pad.repitdoit.features.profile.domain.repository.WorkoutHistoryRepository
import com.pad.repitdoit.features.profile.domain.usecase.GetWorkoutHistoryUseCase

interface AppContainer :
    SessionUseCasesAC,
    ProfileUseCasesAC,
    DummyUseCasesAC,
    GamificationUseCaseAC,
    TrainingUseCasesAC,
    HomeUseCasesAC,
    CatalogueUseCasesAC,
    WorkoutExerciseSetupUseCasesAC
{}


class DefaultAppContainer(
    val db: RepItDoItDatabase? = null,
    val dispatcher: CoroutineDispatcher? = null
) : AppContainer {

    /*
    REPOSITORIES
*/

    private val sessionRepository: SessionRepository =
        if (db == null || dispatcher == null) {
            FakeSessionRepositoryImpl()
        } else {
            SessionRepositoryImpl(db, dispatcher)
        }

    private fun currentUserId(): String? {
        return sessionRepository.activeSession.value?.userId
    }

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

    private val localGamificationRepository =
        if (db != null && dispatcher != null) {
            GamificationRepositoryImpl(db, dispatcher, ::currentUserId)
        } else {
            null
        }

    private val gamificationRepository: GamificationRepository =
        localGamificationRepository ?: FakeGamificationRepositoryImpl()

    private val workoutRepository: WorkoutRepository =
        if (db == null || dispatcher == null) {
            FakeWorkoutRepositoryImpl(::currentUserId)
        } else {
            WorkoutRepositoryImpl(db, dispatcher, ::currentUserId) { userId ->
                localGamificationRepository?.awardCompletedAchievementsInTransaction(userId)
            }
        }

    private val profileRepository: ProfileRepository =
        if (db == null || dispatcher == null) {
            FakeProfileRepositoryImpl(
                sessionRepository as FakeSessionRepositoryImpl
            )
        } else {
            ProfileRepositoryImpl(
                db,
                dispatcher,
                ::currentUserId
            )
        }
    private val workoutHistoryRepository: WorkoutHistoryRepository =
        if (db == null || dispatcher == null) {
            object : WorkoutHistoryRepository {
                override suspend fun getCompletedSessions(
                    userId: String,
                    fromTimestamp: Long,
                    toTimestamp: Long
                ): List<WorkoutHistoryEntry> = emptyList()
            }
        } else {
            WorkoutHistoryRepositoryImpl(db, dispatcher)
        }
        
    private val dummyRepository: DummyRepository =
        if (db == null || dispatcher == null) {
            FakeDummyRepositoryImpl()
        } else {
            DummyRepositoryImpl(db, dispatcher)
        }

    /*
        USE CASES
     */
    override val awardCompletedAchievementsUseCase = AwardCompletedAchievementsUseCase(gamificationRepository)
    override val getAchievementsUseCase = GetAchievementsUseCase(gamificationRepository)
    override val getTotalXpUseCase = GetTotalXpUseCase(gamificationRepository)
    override val getRewardsUseCase = GetRewardsUseCase(gamificationRepository)
    override val completeWorkoutSessionUseCase = CompleteWorkoutSessionUseCase(workoutRepository)
    override val getAllowedAchievementsUseCase = GetAllowedAchievementsUseCase(gamificationRepository)
    override val getHomeDailyWorkoutsUseCase = GetHomeDailyWorkoutsUseCase(homeRepository, ::currentUserId)
    override val getWorkoutUseCase = GetWorkoutUseCase(workoutRepository)
    override val getExerciseContextUseCase = GetExerciseContextUseCase(workoutRepository)
    override val getUsersWorkoutsUseCase = GetUsersWorkoutsUseCase(workoutRepository)
    override val getUsersTrainingPlansUseCase = GetUsersTrainingPlansUseCase(workoutRepository)
    override val selectTrainingPlanUseCase = SelectTrainingPlanUseCase(workoutRepository)
    override val createTrainingPlanUseCase = CreateTrainingPlanUseCase(workoutRepository)
    override val renameTrainingPlanUseCase = RenameTrainingPlanUseCase(workoutRepository)
    override val deleteTrainingPlanUseCase = DeleteTrainingPlanUseCase(workoutRepository)
    override val addWorkoutToPlanUseCase = AddWorkoutToPlanUseCase(workoutRepository)
    override val removeWorkoutFromPlanUseCase = RemoveWorkoutFromPlanUseCase(workoutRepository)
    override val moveWorkoutToPlanDayUseCase = MoveWorkoutToPlanDayUseCase(workoutRepository)
    override val createWorkoutUseCase = CreateWorkoutUseCase(workoutRepository)
    override val getExercisesUseCase = GetExercisesUseCase(catalogueRepository)
    override val getExercisesByMachineUseCase = GetExercisesByMachineUseCase(catalogueRepository)
    override val addExerciseToWorkoutUseCase = AddExerciseToWorkoutUseCase(workoutRepository)
    override val updateWorkoutExerciseUseCase = UpdateWorkoutExerciseUseCase(workoutRepository)
    override val removeExerciseFromWorkoutUseCase = RemoveExerciseFromWorkoutUseCase(workoutRepository)
    override val removeWorkoutUseCase = RemoveWorkoutUseCase(workoutRepository)
    override val renameWorkoutUseCase = RenameWorkoutUseCase(workoutRepository)
    override val getProfileUseCase = GetProfileUseCase(profileRepository)
    override val getWorkoutHistoryUseCase = GetWorkoutHistoryUseCase(workoutHistoryRepository, ::currentUserId)
    override val createUserUseCase = CreateUserUseCase(sessionRepository)
    override val updateUserUseCase = UpdateUserUseCase(profileRepository)
    override val verifyUserUseCase = VerifyUserUseCase(sessionRepository)
    override val loginUseCase = LoginUseCase(sessionRepository)
    override val logoutUseCase = LogoutUseCase(sessionRepository)
    override val getCurrentSessionUseCase = GetCurrentSessionUseCase(sessionRepository)
    override val getMuscleNameByIdUseCase = GetMuscleNameByIdUseCase(dummyRepository)
    override val getExercisesByMuscleUseCase = GetExercisesByMuscleUseCase(dummyRepository)

}
