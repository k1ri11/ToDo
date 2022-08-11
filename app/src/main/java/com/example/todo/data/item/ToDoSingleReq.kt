package com.example.todo.data.item

data class ToDoSingleReq(
    val status: String,
    val element: TodoItemReq,
    val revision: Int,
)
