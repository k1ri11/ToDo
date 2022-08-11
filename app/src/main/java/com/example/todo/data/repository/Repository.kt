package com.example.todo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.data.datasource.network.RetrofitInstance
import com.example.todo.data.model.Importance
import com.example.todo.data.model.lists.TasksListUpdate
import com.example.todo.data.model.mappers.toToDoItem
import com.example.todo.data.model.mappers.toToDoItemRequest
import com.example.todo.data.model.singletask.SingleTaskUpdate
import com.example.todo.domain.model.ToDoItem
import com.example.todo.utils.Resource
import java.util.*

class Repository(private val retrofitInstance: RetrofitInstance) {
    private var revision = 0

    private val _allTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val allTasks: LiveData<Resource<List<ToDoItem>>> = _allTasks

    private val _filteredTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val filteredTasks: LiveData<Resource<List<ToDoItem>>> = _filteredTasks


    private val _singleTask = MutableLiveData<Resource<ToDoItem>>()
    val singleTask: LiveData<Resource<ToDoItem>> = _singleTask

    suspend fun getAllTasks() {
        _allTasks.postValue(Resource.Loading())
        val response = retrofitInstance.api.getAllTasks()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
                val transformedData = body.list.map { it.toToDoItem() }
                _allTasks.postValue(Resource.Success(transformedData))
                val filteredData = transformedData.filter { !it.done }
                _filteredTasks.postValue(Resource.Success(filteredData))
            }
        }
        when (response.code()) {
            400 -> _allTasks.postValue(Resource.Error("Неправильно сформирован запрос"))
            401 -> _allTasks.postValue(Resource.Error("Неверная авторизация"))
            404 -> _allTasks.postValue(Resource.Error("Такой элемент не найден на сервере"))
            500 -> _allTasks.postValue(Resource.Error("Неопознанная ошибка сервера"))
        }
    }

    suspend fun saveTaskList(taskList: List<ToDoItem>) {
        val tmpList = TasksListUpdate(taskList.map { it.toToDoItemRequest() })
        val response =
            retrofitInstance.api.saveTasksList(revision = revision, tasksList = tmpList)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
            }
        }
    }

    suspend fun getTask(id: UUID) {
        _singleTask.postValue(Resource.Loading())
        val response = retrofitInstance.api.getTask(id)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
                _singleTask.postValue(Resource.Success(body.element.toToDoItem()))
            }
        } else {
            when (response.code()) {
                400 -> _singleTask.postValue(Resource.Error("Неправильно сформирован запрос"))
                401 -> _singleTask.postValue(Resource.Error("Неверная авторизация"))
                404 -> _singleTask.postValue(Resource.Error("Такой элемент не найден на сервере"))
                else -> _singleTask.postValue(Resource.Error("Неопознанная ошибка сервера"))
            }
        }
    }


    suspend fun deleteTask(id: UUID) {
        val response = retrofitInstance.api.deleteTask(id = id, revision = revision)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
            }
        }
    }

    suspend fun updateTask(task: ToDoItem) {
        val tmpTask = SingleTaskUpdate(element = task.toToDoItemRequest())
        retrofitInstance.api.updateTask(revision = revision, id = task.id, task = tmpTask)
    }

    suspend fun addTask(task: ToDoItem) {
        val tmpTask = SingleTaskUpdate(element = task.toToDoItemRequest())
        retrofitInstance.api.addTask(task = tmpTask, revision = revision)
    }

    fun changeItemDone(task: ToDoItem) {
        val tmpTaskList = _allTasks.value
        tmpTaskList?.data?.let { list ->
            val index = list.indexOf(task)
            list[index].done = !list[index].done
            _allTasks.postValue(Resource.Success(list))
            val dataFiltered = list.filter { !it.done }
            _filteredTasks.postValue(Resource.Success(dataFiltered))
        }
    }

    fun createEmptyTask(taskId: UUID) {
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
        _singleTask.postValue(Resource.Success(newTask))
    }
}