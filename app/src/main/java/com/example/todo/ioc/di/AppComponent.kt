package com.example.todo.ioc.di

import android.content.Context
import com.example.todo.data.datasource.database.ToDoDatabase
import com.example.todo.data.datasource.network.RetrofitInstance
import com.example.todo.data.repository.Repository
import com.example.todo.ioc.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Qualifier
import javax.inject.Singleton


annotation class ApplicationScope

@ApplicationScope
@Component(modules = [RetrofitInstance::class, ToDoDatabase::class])
interface AppComponent {
    val appContext: Context
    fun activityComponent(): ActivityComponent.Factory
    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance appContext: Context
        ):AppComponent
    }}