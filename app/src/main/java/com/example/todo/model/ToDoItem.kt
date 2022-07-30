package com.example.todo.model

import java.util.*

data class ToDoItem(
    val id: UUID,
    var text: String,
    var importance: String,
    var deadLine: String = "",
    var done: Boolean,
    var color: Int,
    val created_at: String,
    var changed_at: String = "",
)


