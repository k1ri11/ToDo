package com.example.todo.data.model

sealed class Importance() {
    object Low: Importance()
    object High: Importance()
    object Basic: Importance()
}
