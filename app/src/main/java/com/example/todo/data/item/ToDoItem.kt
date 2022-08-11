package com.example.todo.data.item

import com.example.todo.data.Importance
import java.util.*

data class ToDoItem(
    val id: UUID,
    var text: String,
    var importance: Importance,
    var deadLine: Date? = null,
    var done: Boolean,
    var color: Int? = null,
    val createdAt: Date,
    var changedAt: Date,
)


