package com.example.todo.model

import java.util.*

data class ToDoItem(
    val id: UUID,
    var text: String,
    var importance: Importance,
    var deadLine: Date? = null,
    var done: Boolean,
    var color: Int? = null,
    val created_at: Date,
    var changed_at: Date,
)


