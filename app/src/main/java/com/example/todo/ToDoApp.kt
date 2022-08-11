package com.example.todo

import android.app.Application
import android.content.Context
import com.example.todo.ioc.ApplicationComponent

class ToDoApp : Application() {

    val applicationComponent by lazy { ApplicationComponent() }

    companion object {
        fun get(context: Context): ToDoApp = context.applicationContext as ToDoApp
    }
}