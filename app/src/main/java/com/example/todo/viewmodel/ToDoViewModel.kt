package com.example.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    fun getAllTasks() {
        allTasks.value = repository.allTasks.value?.map { it.toToDoItem() }
    }

    fun getSingleTask(id: UUID) {
        singleTask.value = allTasks.value?.find { it.id == id }
    }

    fun createEmptyTask() {
         val emptyTask = TodoItemReq(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            text = "",
            importance = "basic",
            deadLine = 0,
            done = false,
            color = "#000000",
            created_at = System.currentTimeMillis(),
            changed_at = System.currentTimeMillis(),
            last_updated_by = "device"
        )
        singleTask.value = emptyTask.toToDoItem()
    }
}