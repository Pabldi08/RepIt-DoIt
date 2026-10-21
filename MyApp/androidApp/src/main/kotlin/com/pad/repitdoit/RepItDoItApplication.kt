package com.pad.repitdoit

import android.app.Application
import com.pad.repitdoit.core.localdatabase.AndroidDatabaseDriverFactory
import com.pad.repitdoit.core.localdatabase.createDatabase
import com.pad.repitdoit.di.AppContainer
import com.pad.repitdoit.di.DefaultAppContainer
import kotlinx.coroutines.Dispatchers

class RepItDoItApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        val db = createDatabase(
            AndroidDatabaseDriverFactory(applicationContext)
        )
        appContainer = DefaultAppContainer (
            db = db,
            dispatcher = Dispatchers.IO
        )
    }
}