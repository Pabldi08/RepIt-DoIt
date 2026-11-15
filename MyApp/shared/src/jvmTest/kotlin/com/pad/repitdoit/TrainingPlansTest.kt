package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pad.repitdoit.core.localdatabase.LOCAL_TEST_USER_ID
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import com.pad.repitdoit.di.DefaultAppContainer
import com.pad.repitdoit.di.AppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import java.nio.file.Files

class TrainingPlansTest {
    @Test
    fun fakeSelectionKeepsOnlyOneActivePlan() = runBlocking {
        val container = DefaultAppContainer()
        val first = container.createTrainingPlanUseCase("Primero")
        val second = container.createTrainingPlanUseCase("Segundo")
        container.selectTrainingPlanUseCase(first.id)
        container.selectTrainingPlanUseCase(second.id)
        container.selectTrainingPlanUseCase(second.id)
        assertEquals(second.id, container.getUsersTrainingPlansUseCase().single { it.isActive }.id)
        assertFailsWith<IllegalArgumentException> { container.selectTrainingPlanUseCase("missing") }
        assertEquals(second.id, container.getUsersTrainingPlansUseCase().single { it.isActive }.id)
        container.deleteTrainingPlanUseCase(second.id)
        assertTrue(container.getUsersTrainingPlansUseCase().none { it.isActive })
    }

    @Test
    fun selectionPersistsAfterReopeningDatabaseAndRollsBackOnFailure() = runBlocking {
        val file = Files.createTempFile("repitdoit-plan-selection-", ".db")
        try {
            var selectedId = ""
            JdbcSqliteDriver("jdbc:sqlite:${file.toAbsolutePath()}").use { driver ->
                driver.execute(null, "PRAGMA foreign_keys = ON", 0)
                RepItDoItDatabase.Schema.create(driver)
                val db = RepItDoItDatabase(driver)
                testDb(db).apply { loadInitialDatabase(); seed() }
                val container = DefaultAppContainer(db, Dispatchers.Unconfined)
                val first = container.createTrainingPlanUseCase("Primero")
                val second = container.createTrainingPlanUseCase("Segundo")
                selectedId = second.id
                container.selectTrainingPlanUseCase(first.id)
                container.selectTrainingPlanUseCase(second.id)
                assertEquals(second.id, container.getUsersTrainingPlansUseCase().single { it.isActive }.id)

                db.userQueries.insertUser("other-user", "other-user", "Otro usuario", null)
                db.trainingPlanQueries.insertTrainingPlan("foreign-plan", "other-user", "Ajeno", 1L, 1L)
                db.trainingPlanQueries.activateTrainingPlan(1L, "foreign-plan", "other-user")
                assertFailsWith<IllegalArgumentException> { container.selectTrainingPlanUseCase("missing") }
                assertFailsWith<IllegalArgumentException> { container.selectTrainingPlanUseCase("foreign-plan") }
                assertFailsWith<IllegalArgumentException> { container.selectTrainingPlanUseCase(" ") }
                assertEquals(second.id, db.trainingPlanQueries.selectActivePlan(LOCAL_TEST_USER_ID).executeAsOne().id)
                assertEquals("foreign-plan", db.trainingPlanQueries.selectActivePlan("other-user").executeAsOne().id)

                db.trainingPlanQueries.insertTrainingPlan("rejected-plan", LOCAL_TEST_USER_ID, "Rechazado", 1L, 1L)
                driver.execute(null, """
                    CREATE TRIGGER reject_plan_activation BEFORE UPDATE ON TrainingPlan
                    WHEN NEW.id = 'rejected-plan' AND NEW.is_active = 1
                    BEGIN SELECT RAISE(ABORT, 'Activation rejected'); END;
                """.trimIndent(), 0)
                assertFailsWith<Exception> { container.selectTrainingPlanUseCase("rejected-plan") }
                assertEquals(second.id, db.trainingPlanQueries.selectActivePlan(LOCAL_TEST_USER_ID).executeAsOne().id)
            }
            JdbcSqliteDriver("jdbc:sqlite:${file.toAbsolutePath()}").use { driver ->
                val reopened = DefaultAppContainer(RepItDoItDatabase(driver), Dispatchers.Unconfined)
                assertEquals(selectedId, reopened.getUsersTrainingPlansUseCase().single { it.isActive }.id)
            }
        } finally {
            Files.deleteIfExists(file)
        }
    }

    private suspend fun verifyPlanEditing(container: AppContainer, workoutId: String) {
        val plan = container.createTrainingPlanUseCase("  Mi semana  ")
        assertEquals("Mi semana", plan.name)
        container.renameTrainingPlanUseCase(plan.id, "  Semana nueva  ")
        assertEquals("Semana nueva", container.getUsersTrainingPlansUseCase().single { it.id == plan.id }.name)
        assertFailsWith<IllegalArgumentException> { container.createTrainingPlanUseCase("   ") }
        assertFailsWith<IllegalArgumentException> { container.renameTrainingPlanUseCase(plan.id, " ") }
        assertFailsWith<IllegalArgumentException> { container.addWorkoutToPlanUseCase(plan.id, workoutId, 0) }
        assertFailsWith<IllegalArgumentException> { container.addWorkoutToPlanUseCase(plan.id, "missing", 1) }

        container.addWorkoutToPlanUseCase(plan.id, workoutId, 1)
        container.addWorkoutToPlanUseCase(plan.id, workoutId, 1)
        container.addWorkoutToPlanUseCase(plan.id, workoutId, 2)
        var schedule = container.getUsersTrainingPlansUseCase().single { it.id == plan.id }.schedule
        assertEquals(listOf(1, 1, 2), schedule.map { it.weekday })
        assertEquals(listOf(0, 1, 0), schedule.map { it.position })
        assertEquals(3, schedule.map { it.id }.distinct().size)
        val firstId = schedule.first().id

        val otherPlan = container.createTrainingPlanUseCase("Otro plan")
        assertFailsWith<IllegalArgumentException> {
            container.removeWorkoutFromPlanUseCase(otherPlan.id, firstId)
        }
        assertFailsWith<IllegalArgumentException> {
            container.moveWorkoutToPlanDayUseCase(otherPlan.id, firstId, 3)
        }
        assertFailsWith<IllegalArgumentException> {
            container.moveWorkoutToPlanDayUseCase(plan.id, firstId, 8)
        }
        container.moveWorkoutToPlanDayUseCase(plan.id, firstId, 2)
        schedule = container.getUsersTrainingPlansUseCase().single { it.id == plan.id }.schedule
        assertEquals(2, schedule.single { it.id == firstId }.weekday)
        assertEquals(1, schedule.single { it.id == firstId }.position)
        container.moveWorkoutToPlanDayUseCase(plan.id, firstId, 2)
        assertEquals(schedule, container.getUsersTrainingPlansUseCase().single { it.id == plan.id }.schedule)

        container.removeWorkoutFromPlanUseCase(plan.id, firstId)
        schedule = container.getUsersTrainingPlansUseCase().single { it.id == plan.id }.schedule
        assertEquals(2, schedule.size)
        assertTrue(schedule.none { it.id == firstId })
        container.deleteTrainingPlanUseCase(plan.id)
        assertTrue(container.getUsersTrainingPlansUseCase().none { it.id == plan.id })
        assertTrue(container.getWorkoutUseCase(workoutId) != null)
        val zulu = container.createTrainingPlanUseCase("Zulu")
        val alfa = container.createTrainingPlanUseCase("alfa")
        val beta = container.createTrainingPlanUseCase("Beta")
        val ids = setOf(zulu.id, alfa.id, beta.id)
        assertEquals(
            listOf(alfa.id, beta.id, zulu.id),
            container.getUsersTrainingPlansUseCase().filter { it.id in ids }.map { it.id }
        )
        container.renameTrainingPlanUseCase(zulu.id, "Aardvark")
        container.selectTrainingPlanUseCase(beta.id)
        assertEquals(
            listOf(zulu.id, alfa.id, beta.id),
            container.getUsersTrainingPlansUseCase().filter { it.id in ids }.map { it.id }
        )
    }

    @Test
    fun editsPlansInFakeRepository() = runBlocking {
        verifyPlanEditing(DefaultAppContainer(), "001")
    }

    @Test
    fun editsPersistedPlansAndRejectsOtherUsersData() = runBlocking {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            driver.execute(null, "PRAGMA foreign_keys = ON", 0)
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            testDb(db).apply { loadInitialDatabase(); seed() }
            val container = DefaultAppContainer(db, Dispatchers.Unconfined)
            verifyPlanEditing(container, "test-workout-001")

            db.userQueries.insertUser("other-user", "other-user", "Otro usuario", null)
            db.trainingPlanQueries.insertTrainingPlan("foreign-plan", "other-user", "Ajeno", 1L, 1L)
            driver.execute(null, "INSERT INTO Workout(id,user_id,name,created_at,updated_at) VALUES('foreign-workout','other-user','Ajena',1,1)", 0)
            assertFailsWith<IllegalArgumentException> {
                container.renameTrainingPlanUseCase("foreign-plan", "Cambio")
            }
            assertFailsWith<IllegalArgumentException> {
                container.deleteTrainingPlanUseCase("foreign-plan")
            }
            assertFailsWith<IllegalArgumentException> {
                container.addWorkoutToPlanUseCase("foreign-plan", "test-workout-001", 1)
            }
            assertFailsWith<IllegalArgumentException> {
                container.addWorkoutToPlanUseCase("test-plan-001", "foreign-workout", 1)
            }
            assertEquals("Ajeno", db.trainingPlanQueries.selectPlanById("foreign-plan", "other-user").executeAsOne().name)
            val archivedPlan = container.createTrainingPlanUseCase("Prueba de archivo")
            container.removeWorkoutUseCase("test-workout-002")
            assertFailsWith<IllegalArgumentException> {
                container.addWorkoutToPlanUseCase(archivedPlan.id, "test-workout-002", 1)
            }
            container.deleteTrainingPlanUseCase("test-plan-001")
            assertTrue(db.workoutScheduleQueries.selectScheduleByPlan("test-plan-001", LOCAL_TEST_USER_ID)
                .executeAsList().isEmpty())
            assertTrue(container.getWorkoutUseCase("test-workout-001") != null)
        }
    }

    @Test
    fun loadsUserPlansWithOrderedScheduleAndWorkoutSummaries() = runBlocking {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            driver.execute(null, "PRAGMA foreign_keys = ON", 0)
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            testDb(db).apply {
                loadInitialDatabase()
                seed()
            }
            db.trainingPlanQueries.insertTrainingPlan(
                "empty-plan", LOCAL_TEST_USER_ID, "Plan vacío", 2000L, 2000L
            )
            db.userQueries.insertUser("other-user", "other-user", "Otro usuario", null)
            db.trainingPlanQueries.insertTrainingPlan(
                "other-plan", "other-user", "Plan ajeno", 3000L, 3000L
            )

            val load = DefaultAppContainer(db, Dispatchers.Unconfined).getUsersTrainingPlansUseCase
            val plans = load()
            assertEquals(listOf("test-plan-001", "empty-plan"), plans.map { it.id })
            assertTrue(plans.single { it.id == "empty-plan" }.schedule.isEmpty())
            val schedule = plans.single { it.id == "test-plan-001" }.schedule
            assertEquals(14, schedule.size)
            assertEquals((1..7).flatMap { listOf(it, it) }, schedule.map { it.weekday })
            assertEquals(List(7) { listOf(0, 1) }.flatten(), schedule.map { it.position })
            val push = schedule.filter { it.workoutId == "test-workout-001" }
            assertEquals(7, push.size)
            assertTrue(push.all { it.workoutName == "Push Day" && it.numEx == 2 })
            assertTrue(schedule.filter { it.workoutId == "test-workout-002" }
                .all { it.numEx == 0 })

            db.workoutQueries.archiveWorkout(4000L, 4000L, "test-workout-001", LOCAL_TEST_USER_ID)
            val updated = load().single { it.id == "test-plan-001" }.schedule
            assertEquals(7, updated.size)
            assertTrue(updated.all { it.workoutId == "test-workout-002" })
        }
    }
}
