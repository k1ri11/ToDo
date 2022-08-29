package com.example.todo.ui.stateholders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repository.Repository
import com.example.todo.domain.model.ToDoItem
import com.example.todo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ToDoViewModel(
    private val repository: Repository,
) : ViewModel() {
    val allTasks: LiveData<Resource<List<ToDoItem>>> = repository.allTasks

    val filteredTasks: LiveData<Resource<List<ToDoItem>>> = repository.filteredTasks

    val singleTask: LiveData<Resource<ToDoItem>> = repository.singleTask

    var doneIsInvisible = false

    fun getAllTasks() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllTasks()
    }

    fun getTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        repository.getTask(id)
    }

    fun deleteTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(id)
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

    fun changeItemDone(currentItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeItemDone(currentItem)
        }
    }
}