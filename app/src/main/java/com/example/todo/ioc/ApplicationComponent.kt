package com.example.todo.ioc

import android.app.Application
import com.example.todo.ToDoApp
import com.example.todo.data.datasource.network.RetrofitInstance
import com.example.todo.data.repository.Repository

class ApplicationComponent {
    private val networkSource = RetrofitInstance
    private val repository = Repository(networkSource)

    val viewModelFactory = ViewModelFactory(repository)
}