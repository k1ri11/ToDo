package com.example.todo.data.datasource.database

import android.content.Context
import androidx.room.*
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton


@Database(
    entities = [ToDoItem::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ToDoDatabase : RoomDatabase() {


    abstract fun toDoDao(): ToDoDao
}