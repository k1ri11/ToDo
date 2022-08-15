package com.example.todo

import android.app.Application
import android.content.Context
import com.example.todo.ioc.di.AppComponent
import com.example.todo.ioc.di.DaggerAppComponent

class ToDoApp : Application() {

    lateinit var appComponent: AppComponent

    companion object {
        fun get(context: Context): ToDoApp = context.applicationContext as ToDoApp
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}