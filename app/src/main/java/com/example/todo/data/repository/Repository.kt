package com.example.todo.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.R
import com.example.todo.data.datasource.database.ToDoDao
import com.example.todo.data.datasource.database.ToDoDatabase
import com.example.todo.data.datasource.network.ToDoApi
import com.example.todo.data.model.Importance
import com.example.todo.data.model.lists.TasksListUpdate
import com.example.todo.data.model.mappers.toToDoItem
import com.example.todo.data.model.mappers.toToDoItemRequest
import com.example.todo.data.model.singletask.SingleTaskUpdate
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.ApplicationScope
import com.example.todo.utils.Resource
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ApplicationScope
class Repository @Inject constructor(val api: ToDoApi, val database: ToDoDatabase, val context: Context) {

    private var revision = 0
    private val dao = database.cityDao()

    private val _allTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val allTasks: LiveData<Resource<List<ToDoItem>>> = _allTasks

    private val _filteredTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val filteredTasks: LiveData<Resource<List<ToDoItem>>> = _filteredTasks


    private val _singleTask = MutableLiveData<Resource<ToDoItem>>()
    val singleTask: LiveData<Resource<ToDoItem>> = _singleTask

    suspend fun getAllTasks(hasNetwork: Boolean) {
        _allTasks.postValue(Resource.Loading())
        if (hasNetwork) {
            val response = api.getAllTasks()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    revision = body.revision
                    val loadedData = body.list.map { it.toToDoItem() }
                    dao.saveTasksList(loadedData)
                }
            }
            when (response.code()) {
                400 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.incorrect_request)))
                401 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.incorrect_authorization)))
                404 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.element_not_found)))
                500 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.server_error)))
            }
        }
        _allTasks.postValue(Resource.Success(dao.getAllTasks()))
        _filteredTasks.postValue(Resource.Success(dao.getFilteredTasks()))
    }

    suspend fun saveTaskList(taskList: List<ToDoItem>, hasNetwork: Boolean) {
        dao.saveTasksList(taskList)
        if (hasNetwork) {
            val tmpList = TasksListUpdate(taskList.map { it.toToDoItemRequest() })
            val response = api.saveTasksList(revision = revision, tasksList = tmpList)
            if (response.isSuccessful) {
                response.body()?.let { body -> revision = body.revision }
            }
        }
    }

    suspend fun getTask(id: UUID, hasNetwork: Boolean) {
        _singleTask.postValue(Resource.Loading())
        if (hasNetwork) {
            val response = api.getTask(id)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    revision = body.revision
                    val loadedData = body.element.toToDoItem()
                    dao.updateTask(loadedData)
                }
            } else {
                when (response.code()) {
                    400 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.incorrect_request)))
                    401 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.incorrect_authorization)))
                    404 -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.element_not_found)))
                    else -> _allTasks.postValue(Resource.Error(context.resources.getString(R.string.server_error)))
                }
            }
        }
        _singleTask.postValue(Resource.Success(dao.getTask(id)))
    }


    suspend fun deleteTask(task: ToDoItem, hasNetwork: Boolean) {
        dao.deleteTask(task)
        if (hasNetwork) {
            val response = api.deleteTask(id = task.id, revision = revision)
            if (response.isSuccessful) {
                response.body()?.let { body -> revision = body.revision }
            }
        }
        getAllTasks(hasNetwork)
    }

    suspend fun updateTask(task: ToDoItem, hasNetwork: Boolean) {
        dao.updateTask(task)
        if (hasNetwork) {
            val tmpTask = SingleTaskUpdate(element = task.toToDoItemRequest())
            api.updateTask(revision = revision, id = task.id, task = tmpTask)
        }
        getAllTasks(hasNetwork)
    }

    suspend fun addTask(task: ToDoItem, hasNetwork: Boolean) {
        dao.addTask(task)
        if (hasNetwork) {
            val tmpTask = SingleTaskUpdate(element = task.toToDoItemRequest())
            api.addTask(task = tmpTask, revision = revision)
        }
        getAllTasks(hasNetwork)
    }

    fun changeItemDone(task: ToDoItem) {
        val tmpTaskList = _allTasks.value
        tmpTaskList?.data?.let { list ->
            val index = list.indexOf(task)
            list[index].done = !list[index].done
            list[index].changedAt = Date(System.currentTimeMillis())
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