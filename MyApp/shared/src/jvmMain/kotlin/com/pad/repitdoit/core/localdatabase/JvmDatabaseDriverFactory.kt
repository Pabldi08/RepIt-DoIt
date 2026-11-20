package com.pad.repitdoit.core.localdatabase

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.util.Properties

class JvmDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val appData = System.getenv("APPDATA")
            ?: error("APPDATA no disponible")

        val dbDirectory = File(appData, "RepItDoIt")

        check ( dbDirectory.isDirectory || dbDirectory.mkdir() ) {
            "No se pudo crear el directorio de la BD"
        }

        val dbFile = File (
            dbDirectory,
            "repitdoit.db"
        )

        val properties = Properties().apply {
            setProperty("foreign_keys", "true")
        }

        return JdbcSqliteDriver (
            "jdbc:sqlite:${dbFile.absolutePath}",
            properties,
            RepItDoItDatabase.Schema
        )
    }
}