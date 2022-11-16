package com.example.todo.data.datasource.database

import androidx.room.*
import com.example.todo.domain.model.ToDoItem
import java.util.*

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_items")
    suspend fun getAllTasks(): List<ToDoItem>

    @Query("SELECT * FROM todo_items WHERE todo_items.done = 0")
    suspend fun getFilteredTasks(): List<ToDoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTasksList(tasks: List<ToDoItem>)

    @Query("SELECT * FROM todo_items WHERE todo_items.id = :taskId ")
    suspend fun getTask(taskId: UUID): ToDoItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: ToDoItem)

    @Update
    suspend fun updateTask(task: ToDoItem)

    @Delete
    suspend fun deleteTask(task: ToDoItem)
}