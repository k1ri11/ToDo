package com.example.todo.data.network.repository

import com.example.todo.data.item.ToDoItem
import com.example.todo.data.item.ToDoSingleUpd
import com.example.todo.data.lists.ToDoListUpd
import com.example.todo.data.network.RetrofitInstance
import com.example.todo.data.toToDoItem
import com.example.todo.data.toToDoItemReq
import com.example.todo.utils.Resource
import java.util.*

class TodoItemsRepository {
    private var revision = 0

    suspend fun getAllTasks(): Resource<List<ToDoItem>> {
        val response = RetrofitInstance.api.getAllTasks()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
                return Resource.Success(body.list.map { it.toToDoItem() })
            }
        }
        return when (response.code()) {
            400 -> Resource.Error("Неправильно сформирован запрос")
            401 -> Resource.Error("Неверная авторизация")
            404 -> Resource.Error("Такой элемент не найден на сервере")
            else -> Resource.Error("Неопознанная ошибка сервера")
        }
    }

    suspend fun saveTaskList(taskList: List<ToDoItem>) {
        val tmpList = ToDoListUpd(taskList.map { it.toToDoItemReq() })
        val response = RetrofitInstance.api.saveTasksList(revision = revision, tasksList = tmpList)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
            }
        }
    }

    suspend fun getTask(id: UUID): Resource<ToDoItem> {
        val response = RetrofitInstance.api.getTask(id = id)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
                return Resource.Success(body.element.toToDoItem())
            }
        }
        return when (response.code()) {
            400 -> Resource.Error("Неправильно сформирован запрос")
            401 -> Resource.Error("Неверная авторизация")
            404 -> Resource.Error("Такой элемент не найден на сервере")
            else -> Resource.Error("Неопознанная ошибка сервера")
        }
    }

    suspend fun deleteTask(id: UUID) {
        val response = RetrofitInstance.api.deleteTask(id = id, revision = revision)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                revision = body.revision
            }
        }
    }

    suspend fun updateTask(task: ToDoItem) {
        val tmpTask = ToDoSingleUpd(element = task.toToDoItemReq())
        RetrofitInstance.api.updateTask(revision = revision, id = task.id, task = tmpTask)
    }

    suspend fun addTask(task: ToDoItem) {
        val tmpTask = ToDoSingleUpd(element = task.toToDoItemReq())
        RetrofitInstance.api.addTask(task = tmpTask, revision = revision)
    }

}