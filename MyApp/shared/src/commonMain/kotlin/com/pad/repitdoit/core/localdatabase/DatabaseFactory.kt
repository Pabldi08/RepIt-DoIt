package com.pad.repitdoit.core.localdatabase

fun createDatabase(
    driverFactory: DatabaseDriverFactory
) : RepItDoItDatabase {
    val driver = driverFactory.createDriver()

    return RepItDoItDatabase(driver)
}