package com.pad.repitdoit

import android.app.Application
import com.pad.repitdoit.core.localdatabase.AndroidDatabaseDriverFactory
import com.pad.repitdoit.core.localdatabase.createDatabase
import com.pad.repitdoit.core.localdatabase.testDb
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.di.DefaultAppContainer
import kotlinx.coroutines.Dispatchers

// Prepara las dependencias de la aplicación para el proceso de Android.
class RepItDoItApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()


        val db = createDatabase(
            AndroidDatabaseDriverFactory(applicationContext)
        )
        testDb(db).apply {
            loadInitialDatabase()
            seedDemo()
        }
        appContainer = DefaultAppContainer (
            db = db,
            dispatcher = Dispatchers.IO
        )
    }
}
