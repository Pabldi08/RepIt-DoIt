package com.pad.repitdoit

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pad.repitdoit.core.localdatabase.RepItDoItDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserXpTest {
    @Test
    fun repositoryLoadsXpForTheRequestedUser() = kotlinx.coroutines.runBlocking {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            db.userQueries.insertUser("first", "first", "First", null)
            db.userQueries.insertUser("second", "second", "Second", null)
            driver.execute(null, "UPDATE User SET total_xp = 180 WHERE id = 'first'", 0)
            val repository = com.pad.repitdoit.features.gamification.data.repository.GamificationRepositoryImpl(
                db, kotlinx.coroutines.Dispatchers.Unconfined, { "first" }
            )
            assertEquals(180L, com.pad.repitdoit.features.gamification.domain.usecase.GetTotalXpUseCase(repository)())
            assertEquals(0L, com.pad.repitdoit.features.gamification.data.repository.GamificationRepositoryImpl(
                db, kotlinx.coroutines.Dispatchers.Unconfined, { "second" }
            ).getTotalXp())
        }
    }

    @Test
    fun newUsersStartWithZeroXpAndRejectNegativeValues() {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            db.userQueries.insertUser("user", "username", "Name", null)
            assertEquals(0L, db.userQueries.selectUserById("user").executeAsOne().total_xp)
            driver.execute(null, "UPDATE User SET total_xp = 150 WHERE id = 'user'", 0)
            db.userQueries.updateUserProfile("new-name", "New name", "user")
            assertEquals(150L, db.userQueries.selectUserById("user").executeAsOne().total_xp)
            assertFailsWith<java.sql.SQLException> {
                driver.execute(null, "UPDATE User SET total_xp = -1 WHERE id = 'user'", 0)
            }
        }
    }

    @Test
    fun migrationPreservesExistingUsersAndInitializesXp() {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).use { driver ->
            RepItDoItDatabase.Schema.create(driver)
            val db = RepItDoItDatabase(driver)
            db.userQueries.insertUser("user", "username", "Name", null)
            driver.execute(null, "ALTER TABLE User DROP COLUMN total_xp", 0)
            driver.execute(null, "ALTER TABLE Achievement DROP COLUMN xp_reward", 0)
            RepItDoItDatabase.Schema.migrate(driver, 4, RepItDoItDatabase.Schema.version)
            val user = db.userQueries.selectUserById("user").executeAsOne()
            assertEquals("Name", user.display_name)
            assertEquals(0L, user.total_xp)
            assertFailsWith<java.sql.SQLException> {
                driver.execute(null, "UPDATE User SET total_xp = -1 WHERE id = 'user'", 0)
            }
        }
    }
}
