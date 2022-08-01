package com.example.todo.model

import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

data class TodoItemReq(
    val id: UUID,
    var text: String,
    var importance: Importance,
    val deadLine: Long? = null,
    var done: Boolean,
    var color: String? = null,
    val created_at: Long,
    var changed_at: Long,
    var last_updated_by: String
) {
    fun toToDoItem(): ToDoItem {
        return ToDoItem(
            id = id,
            text = text,
            importance = importance,
            deadLine = if (deadLine != null) Date(deadLine) else null,
            done = done,
            color = Color.parseColor(color),
            created_at = Date(created_at),
            changed_at = Date(changed_at),
        )
    }
}




