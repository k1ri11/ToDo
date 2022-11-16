package com.example.todo.ui.stateholders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repository.Repository
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.ActivityScope
import com.example.todo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ActivityScope
class ToDoViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {
    val allTasks: LiveData<Resource<List<ToDoItem>>> = repository.allTasks

    val filteredTasks: LiveData<Resource<List<ToDoItem>>> = repository.filteredTasks

    val singleTask: LiveData<Resource<ToDoItem>> = repository.singleTask

    var doneIsInvisible = false

    init {
        getAllTasks()
    }

    fun getAllTasks() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllTasks()
    }

    fun getTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        repository.getTask(id)
    }

    fun deleteTask(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(task)
    }

    fun saveTaskList(taskList: List<ToDoItem>) = viewModelScope.launch(Dispatchers.IO) {
            repository.saveTaskList(taskList)
        }

    fun updateTask(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask(task)
    }

    fun addTask(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTask(task)
    }

    fun createEmptyTask(taskId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createEmptyTask(taskId)
        }
    }
}