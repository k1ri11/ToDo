package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.repository.TodoItemsRepository
import com.example.todo.viewmodel.ToDoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ToDoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = TodoItemsRepository()
        viewModel = ToDoViewModel(application, repository)
    }
}