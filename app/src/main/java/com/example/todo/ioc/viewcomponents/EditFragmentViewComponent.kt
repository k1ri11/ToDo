package com.example.todo.ioc.viewcomponents

import androidx.lifecycle.LifecycleOwner
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.ioc.ToDoFragmentComponent
import com.example.todo.ui.view.controllers.EditViewController

class EditFragmentViewComponent (
    fragmentComponent: ToDoFragmentComponent,
    root: FragmentEditBinding,
    viewLifecycleOwner: LifecycleOwner,
) {
    val editViewController = EditViewController(
        fragmentComponent.fragment,
        root,
        viewLifecycleOwner,
        fragmentComponent.viewModel,
    )
}