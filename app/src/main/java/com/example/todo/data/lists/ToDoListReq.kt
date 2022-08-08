package com.example.todo.data.lists

import com.example.todo.data.item.TodoItemReq

data class ToDoListReq(
    val status: String,
    val list: List<TodoItemReq>,
    val revision: Int
)