package com.example.todo.ioc.di

import android.content.Context
import com.example.todo.ioc.ViewModelFactory
import dagger.BindsInstance
import dagger.Component


annotation class ApplicationScope

@ApplicationScope
@Component(modules = [AppModule::class])
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