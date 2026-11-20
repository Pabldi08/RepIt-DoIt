package com.pad.repitdoit.core.localdatabase

// Crea la base de datos a partir del controlador de la plataforma.
fun createDatabase(
    driverFactory: DatabaseDriverFactory
) : RepItDoItDatabase {
    val driver = driverFactory.createDriver()

    return RepItDoItDatabase(driver)
}