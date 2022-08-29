package com.example.todo.domain.model

import com.example.todo.data.model.Importance
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


