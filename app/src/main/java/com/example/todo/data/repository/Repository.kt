package com.example.todo.data.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.R
import com.example.todo.data.datasource.database.ToDoDao
import com.example.todo.data.datasource.network.ToDoApi
import com.example.todo.data.model.Importance
import com.example.todo.data.model.lists.TasksListUpdate
import com.example.todo.data.model.mappers.toToDoItem
import com.example.todo.data.model.mappers.toToDoItemRequest
import com.example.todo.data.model.singletask.SingleTaskUpdate
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.ApplicationScope
import com.example.todo.ui.view.NetworkUtils
import com.example.todo.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ApplicationScope
class Repository @Inject constructor(
    private val api: ToDoApi,
    private val dao: ToDoDao,
    val context: Context,
    private val networkUtils: NetworkUtils,
) {

    private val prefs = context.getSharedPreferences("storage", Context.MODE_PRIVATE)

    private val _allTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val allTasks: LiveData<Resource<List<ToDoItem>>> = _allTasks

    private val _filteredTasks = MutableLiveData<Resource<List<ToDoItem>>>()
    val filteredTasks: LiveData<Resource<List<ToDoItem>>> = _filteredTasks

    private val _singleTask = MutableLiveData<Resource<ToDoItem>>()
    val singleTask: LiveData<Resource<ToDoItem>> = _singleTask

    private var connectionState = networkUtils.hasInternetConnection()

    init {
        setupObserver()
    }

    private fun updateRevision(revision: Int){
        prefs.edit {
            putInt("revision", revision)
        }
    }

    private fun getRevision(): Int{
        return prefs.getInt("revision", 0)
    }

    private fun setupObserver() {
        networkUtils.getNetworkLiveData().observeForever { isConnected ->
            if (isConnected && !connectionState) {
                connectionState = isConnected
                CoroutineScope(Dispatchers.IO).launch {
                    allTasks.value?.data?.let { saveTaskList(it) }
                }
            }
            if (!isConnected) {
                connectionState = isConnected
            }
        }
    }

    suspend fun getAllTasks() {
        _allTasks.postValue(Resource.Loading())
        if (networkUtils.hasInternetConnection()) {
            val response = api.getAllTasks()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    updateRevision(body.revision)
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
        } else {
            _allTasks.postValue(Resource.Error(context.resources.getString(R.string.no_internet_connection)))
        }
        _allTasks.postValue(Resource.Success(dao.getAllTasks()))
        _filteredTasks.postValue(Resource.Success(dao.getFilteredTasks()))
    }

    suspend fun saveTaskList(taskList: List<ToDoItem>) {
        dao.saveTasksList(taskList)
        if (networkUtils.hasInternetConnection()) {
            val tmpList = TasksListUpdate(taskList.map { it.toToDoItemRequest() })
            val response = api.saveTasksList(revision = getRevision(), tasksList = tmpList)
            if (response.isSuccessful) {
                response.body()?.let { body -> updateRevision(body.revision)}
            }
        }
    }

    suspend fun getTask(id: UUID) {
        _singleTask.postValue(Resource.Loading())
        if (networkUtils.hasInternetConnection()) {
            val response = api.getTask(id)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    updateRevision(body.revision)
                    val loadedData = body.element.toToDoItem()
                    dao.updateTask(loadedData)
                }
            } else {
                when (response.code()) {
                    400 -> _singleTask.postValue(Resource.Error(context.resources.getString(R.string.incorrect_request)))
                    401 -> _singleTask.postValue(Resource.Error(context.resources.getString(R.string.incorrect_authorization)))
                    404 -> _singleTask.postValue(Resource.Error(context.resources.getString(R.string.element_not_found)))
                    else -> _singleTask.postValue(Resource.Error(context.resources.getString(R.string.server_error)))
                }
            }
        } else {
            _singleTask.postValue(Resource.Error(context.resources.getString(R.string.no_internet_connection)))
        }
        _singleTask.postValue(Resource.Success(dao.getTask(id)))
    }


    suspend fun deleteTask(task: ToDoItem) {
        dao.deleteTask(task)
        if (networkUtils.hasInternetConnection()) {
            val response = api.deleteTask(id = task.id, revision = getRevision())
            if (response.isSuccessful) {
                response.body()?.let { body -> updateRevision(body.revision) }
            }
        }
        getAllTasks()
    }

    suspend fun updateTask(task: ToDoItem) {
        dao.updateTask(task)
        if (networkUtils.hasInternetConnection()) {
            val tmpTask = SingleTaskUpdate(element = task.toToDoItemRequest())
            api.updateTask(revision = getRevision(), id = task.id, task = tmpTask)
        }
        getAllTasks()
    }

    suspend fun addTask(task: ToDoItem) {
        dao.addTask(task)
        if (networkUtils.hasInternetConnection()) {
            val tmpTask = SingleTaskUpdate(element = task.toToDoItemRequest())
            api.addTask(task = tmpTask, revision = getRevision())
        }
        getAllTasks()
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