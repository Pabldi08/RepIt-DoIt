package com.pad.repitdoit.core.localdatabase

import app.cash.sqldelight.db.SqlDriver

// Define cómo se obtiene un controlador de base de datos en cada plataforma.
interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}