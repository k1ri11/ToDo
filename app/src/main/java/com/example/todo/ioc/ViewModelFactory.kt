package com.example.todo.ioc

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.repository.Repository
import com.example.todo.ioc.di.ApplicationScope
import com.example.todo.ui.stateholders.ToDoViewModel
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScope
class ViewModelFactory @Inject constructor (private val repository: Repository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {
            return ToDoViewModel(repository = repository ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
