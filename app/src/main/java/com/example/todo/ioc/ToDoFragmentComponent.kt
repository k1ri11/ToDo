package com.example.todo.ioc

import androidx.fragment.app.Fragment
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.TaskAdapter

class ToDoFragmentComponent(
    val applicationComponent: ApplicationComponent,
    val fragment: Fragment,
    val viewModel: ToDoViewModel,
) {
    val adapter = TaskAdapter(viewModel, fragment)
}