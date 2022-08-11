package com.example.todo.data

sealed class Importance() {
    object Low: Importance()
    object High: Importance()
    object Basic: Importance()
}
