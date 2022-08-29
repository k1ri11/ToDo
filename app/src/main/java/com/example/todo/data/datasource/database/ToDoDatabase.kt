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


@Module
@Database(
    entities = [ToDoItem::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ToDoDatabase : RoomDatabase() {


    abstract fun cityDao(): ToDoDao

    companion object {
        private var INSTANCE: ToDoDatabase? = null

        @Provides
        fun getToDoDatabase (context: Context): ToDoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}