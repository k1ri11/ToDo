package com.example.todo

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.data.network.repository.TodoItemsRepository
import com.example.todo.viewmodel.ToDoViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: ToDoViewModel by viewModels {
        ToDoAndroidViewModelFactory(
            application,
            TodoItemsRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                v.clearFocus()
                val imm: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }
}