package com.example.todo.data.model.lists

import com.example.todo.data.model.ToDoItemRequest

data class TasksListRequest(
    val status: String,
    val list: List<ToDoItemRequest>,
    val revision: Int
)