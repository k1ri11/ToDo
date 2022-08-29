package com.example.todo.data.repository

import android.graphics.Color
import com.example.todo.data.model.Importance
import com.example.todo.data.model.lists.TasksListRequest
import com.example.todo.data.model.mappers.toToDoItemRequest
import com.example.todo.data.model.singletask.SingleTaskRequest
import com.example.todo.domain.model.ToDoItem
import java.util.*

class TestResponse {
    companion object {

        val TasksList = listOf(
            ToDoItem(
                id = UUID.fromString("e1f90023-0755-4ae7-9cd4-809af52b988c"),
                text = "56789",
                importance = Importance.Basic,
                deadLine = null,
                done = false,
                color = Color.parseColor("#000000"),
                createdAt = Date(1661143529821),
                changedAt = Date(1661253969520),
            )
        )

        val taskListRequest = TasksListRequest(
            "ok",
            TasksList.map { it.toToDoItemRequest() },
            0
        )

        val singleTaskRequest = SingleTaskRequest(
            "ok",
            TasksList[0].toToDoItemRequest(),
            0
        )
    }
}