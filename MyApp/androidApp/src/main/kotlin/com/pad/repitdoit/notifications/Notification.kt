package com.pad.repitdoit.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pad.repitdoit.navigation.Route
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

// Reúne la configuración de las notificaciones de Android.
object Notification {
    fun scheduleNotification(context: Context, title: String, message: String, delaySeconds: Long, toScreen: Route = Route.Home
    ) {
        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .putString("route", Json.encodeToString(Route.serializer(), toScreen))
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}