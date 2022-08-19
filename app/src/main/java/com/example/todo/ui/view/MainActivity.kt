package com.example.todo.ui.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.ToDoApp
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.ioc.di.ActivityComponent
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.workmanager.ToDoWorkManager.Companion.startWorker


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        val appComponent = ToDoApp.get(applicationContext).appComponent

        val viewModel: ToDoViewModel by viewModels {
            appComponent.getViewModelFactory()
        }
        activityComponent = appComponent.activityComponent().create(viewModel)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view: View? = currentFocus
            if (view is EditText) {
                view.clearFocus()
                val inputManager: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }
}