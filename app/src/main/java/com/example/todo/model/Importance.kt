package com.example.todo.model

sealed class Importance() {
    object Low: Importance()
    object High: Importance()
    object Basic: Importance()
}
