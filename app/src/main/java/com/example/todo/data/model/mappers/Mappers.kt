package com.example.todo.data.model.mappers

import android.graphics.Color
import com.example.todo.data.model.Importance
import com.example.todo.domain.model.ToDoItem
import com.example.todo.data.model.ToDoItemRequest
import java.util.*

fun ToDoItem.toToDoItemRequest(): ToDoItemRequest {
    return ToDoItemRequest(
        id = id,
        text = text,
        importance = when (importance) {
            Importance.Low -> "low"
            Importance.High -> "important"
            else -> "basic"
        },
        deadLine = deadLine?.time,
        done = done,
        color = "#000000",
        createdAt = createdAt.time,
        changedAt = changedAt.time,
        lastUpdatedBy = "1"
    )
}


fun ToDoItemRequest.toToDoItem(): ToDoItem {
    return ToDoItem(
        id = id,
        text = text,
        importance = when (importance) {
            "low" -> Importance.Low
            "important" -> Importance.High
            else -> Importance.Basic
        },
        deadLine = deadLine?.let { Date(it) },
        done = done,
        color = Color.parseColor(color),
        createdAt = Date(createdAt),
        changedAt = Date(changedAt),
    )
}