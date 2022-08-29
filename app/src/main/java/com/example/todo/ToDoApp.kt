package com.example.todo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.todo.ioc.di.AppComponent
import com.example.todo.ioc.di.DaggerAppComponent
import com.example.todo.workmanager.ToDoWorkManager.Companion.startWorker


class ToDoApp : Application() {

    lateinit var appComponent: AppComponent

    companion object {
        fun get(context: Context): ToDoApp = context.applicationContext as ToDoApp
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this.applicationContext)
        createNotification()
        this.applicationContext.startWorker()
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "updating_task_channel",
                "Updating tasks",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}