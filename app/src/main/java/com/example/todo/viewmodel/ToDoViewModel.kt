package com.example.todo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todo.model.Importance
import com.example.todo.model.ToDoItem
import com.example.todo.model.TodoItemReq
import com.example.todo.repository.TodoItemsRepository
import java.util.*

class ToDoViewModel(
    app: Application,
    private val repository: TodoItemsRepository,
) : AndroidViewModel(app) {
    val allTasks = MutableLiveData<List<ToDoItem>>()
    val singleTask = MutableLiveData<ToDoItem>()
    var doneVisibility = false

    fun getAllTasks() {
        allTasks.value = repository.getAllTasks().map { it.toToDoItem() }
    }

    fun getSingleTask(id: UUID) {
        singleTask.value = repository.getSingleTask(id)?.toToDoItem()
    }

    fun deleteTask(id: UUID) {
        repository.deleteTask(id)
    }

    fun saveTask(task: ToDoItem) {
        val taskReq = TodoItemReq(
            id = task.id,
            text = task.text,
            importance = task.importance,
            deadLine = task.deadLine?.time,
            done = task.done,
            color = "#000000",
            created_at = task.created_at.time,
            changed_at = task.changed_at.time,
            last_updated_by = "device"
        )
        repository.saveTask(taskReq)
    }

    fun createEmptyTask() {
        val emptyTask = TodoItemReq(
            id = UUID.randomUUID(),
            text = "",
            importance = Importance.Basic,
            deadLine = null,
            done = false,
            color = "#000000",
            created_at = System.currentTimeMillis(),
            changed_at = System.currentTimeMillis(),
            last_updated_by = "device"
        )
        singleTask.value = emptyTask.toToDoItem()
    }
}