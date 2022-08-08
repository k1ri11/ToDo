package com.example.todo.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.ToDoApp
import com.example.todo.data.Importance
import com.example.todo.data.item.ToDoItem
import com.example.todo.data.network.repository.TodoItemsRepository
import com.example.todo.utils.Resource
import kotlinx.coroutines.*
import java.util.*

class ToDoViewModel(
    app: Application,
    private val repository: TodoItemsRepository,
) : AndroidViewModel(app) {
    val allTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val filteredTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val singleTask = MutableLiveData<Resource<ToDoItem>>()
    var doneIsInvisible = false

    fun getAllTasks() = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()) {
            allTasks.postValue(Resource.Loading())
            val response = repository.getAllTasks()
            allTasks.postValue(response)
            if (response is Resource.Success) {
                val dataFiltered = response.data?.filter { !it.done }
                dataFiltered?.let { filteredTasks.postValue(Resource.Success(it)) }
            }
        } else {
            allTasks.postValue(Resource.Error("Нет доступа в интернет"))
        }

    }

    fun getTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()) {
            singleTask.postValue(Resource.Loading())
            val response = repository.getTask(id)
            singleTask.postValue(response)
        } else {
            singleTask.postValue(Resource.Error("Нет доступа в интернет"))
        }
    }

    fun deleteTask(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()) {
            repository.deleteTask(id)
        }
    }

    fun createEmptyTask(taskId: UUID): ToDoItem {
        val newTask = ToDoItem(
            id = taskId,
            text = "",
            importance = Importance.Basic,
            deadLine = null,
            done = false,
            color = 0,
            createdAt = Date(System.currentTimeMillis()),
            changedAt = Date(System.currentTimeMillis()),
        )
        singleTask.value = Resource.Success(newTask)
        return newTask
    }

    fun saveTaskList(taskList: List<ToDoItem>) = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()) {
            repository.saveTaskList(taskList)
        }
    }

    fun updateTask(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()) {
            repository.updateTask(task)
        }
    }

    fun addTask(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()) {
            repository.addTask(task)
        }
    }

    fun changeItemDone(task: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        val tmpTaskList = allTasks.value
        tmpTaskList?.data?.let { list ->
            val index = list.indexOf(task)
            list[index].done = !list[index].done
            allTasks.postValue(Resource.Success(list))
            val dataFiltered = list.filter { !it.done }
            filteredTasks.postValue(Resource.Success(dataFiltered))
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ToDoApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}