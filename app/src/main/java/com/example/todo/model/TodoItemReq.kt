package com.example.todo.model

import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

data class TodoItemReq(
    val id: UUID,
    var text: String,
    var importance: String,
    var deadLine: Long,
    var done: Boolean,
    var color: String,
    val created_at: Long,
    var changed_at: Long,
    var last_updated_by: String
) {
    fun toToDoItem(): ToDoItem {
        return ToDoItem(
            id = id,
            text = text,
            importance = importance,
            deadLine = if (deadLine == 0L) "" else SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(Date(deadLine)
            ),
            done = done,
            color = Color.parseColor(color),
            created_at = SimpleDateFormat("'created at' yyyy.MM.dd HH:mm:ss z").format(Date(created_at)),
            changed_at = SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(Date(changed_at)),
        )
    }
}




