package com.example.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.model.Importance
import com.example.todo.model.ToDoItem
import com.example.todo.model.TodoItemReq
import com.example.todo.repository.TodoItemsRepository
import kotlinx.coroutines.*
import java.util.*

class ToDoViewModel(
    app: Application,
    private val repository: TodoItemsRepository,
) : AndroidViewModel(app) {
    val allTasks = MutableLiveData<List<ToDoItem>>()
    val singleTask = MutableLiveData<ToDoItem>()
    var doneIsInvisible = false

    fun getAllTasks() = viewModelScope.launch(Dispatchers.IO) {
        val tmpTasks = repository.getAllTasks()
        withContext(Dispatchers.Main) {
            allTasks.value = tmpTasks
        }
    }

    fun getSingleTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val tmpTasks = repository.getSingleTask(id)
        tmpTasks?.let {
            withContext(Dispatchers.Main) {
                singleTask.value = it
            }
        }
    }

    fun deleteTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(id)
    }

    fun saveTask(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTask(task)
    }

    fun createEmptyTask(taskId: UUID): ToDoItem {
        val newTask = ToDoItem(
            id = taskId,
            text = "",
            importance = Importance.Basic,
            deadLine = null,
            done = false,
            color = 0,
            created_at = Date(System.currentTimeMillis()),
            changed_at = Date(System.currentTimeMillis()),
        )
        singleTask.value = newTask
        return newTask
    }

    fun saveTaskList(taskList: List<ToDoItem>) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTaskList(taskList)
    }
}