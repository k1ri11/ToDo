package com.example.todo.data.model.singletask

import com.example.todo.data.model.ToDoItemRequest

data class SingleTaskRequest(
    val status: String,
    val element: ToDoItemRequest,
    val revision: Int,
)
