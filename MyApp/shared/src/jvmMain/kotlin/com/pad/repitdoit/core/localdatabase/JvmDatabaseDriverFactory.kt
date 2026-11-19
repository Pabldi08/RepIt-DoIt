package com.pad.repitdoit.core.localdatabase

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.nio.file.Files
import java.util.Properties

class JvmDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val dbDirectory = File(applicationDataDirectory(), "RepItDoIt")
        Files.createDirectories(dbDirectory.toPath())
        val dbFile = File(dbDirectory, "repitdoit.db")

        val properties = Properties().apply {
            setProperty("foreign_keys", "true")
        }

        return JdbcSqliteDriver (
            "jdbc:sqlite:${dbFile.absolutePath}",
            properties,
            RepItDoItDatabase.Schema
        )
    }

    private fun applicationDataDirectory(): File {
        System.getenv("APPDATA")?.takeIf { it.isNotBlank() }?.let { return File(it) }

        val home = File(System.getProperty("user.home"))
        val osName = System.getProperty("os.name")
        return when {
            osName.startsWith("Windows", ignoreCase = true) -> File(home, "AppData/Roaming")
            osName.startsWith("Mac", ignoreCase = true) -> File(home, "Library/Application Support")
            else -> System.getenv("XDG_DATA_HOME")
                ?.takeIf { it.isNotBlank() }
                ?.let(::File)
                ?.takeIf { it.isAbsolute }
                ?: File(home, ".local/share")
        }
    }
}
