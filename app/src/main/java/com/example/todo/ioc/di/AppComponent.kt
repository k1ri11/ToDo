package com.example.todo.ioc.di

import com.example.todo.data.datasource.network.RetrofitInstance
import com.example.todo.data.repository.Repository
import com.example.todo.ioc.ViewModelFactory
import dagger.Component
import javax.inject.Singleton


annotation class ApplicationScope

@Singleton
@ApplicationScope
@Component(modules = [RetrofitInstance::class])
interface AppComponent {
    fun activityComponent(): ActivityComponent.Factory
    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create():AppComponent
    }}