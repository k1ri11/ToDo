package com.example.todo.data

import android.graphics.Color
import com.example.todo.data.item.ToDoItem
import com.example.todo.data.item.TodoItemReq
import java.util.*

fun ToDoItem.toToDoItemReq(): TodoItemReq {
    return TodoItemReq(
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
        lastUpdatedBby = "1"
    )
}


fun TodoItemReq.toToDoItem(): ToDoItem{
    return ToDoItem(
        id = id,
        text = text,
        importance = when (importance) {
            "low" -> Importance.Low
            "important" -> Importance.High
            else -> Importance.Basic
        },
        deadLine = if (deadLine != null) Date(deadLine) else null,
        done = done,
        color = Color.parseColor(color),
        createdAt = Date(createdAt),
        changedAt = Date(changedAt),
    )
}