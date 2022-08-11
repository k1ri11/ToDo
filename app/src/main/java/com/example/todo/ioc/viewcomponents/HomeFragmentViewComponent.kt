package com.example.todo.ioc.viewcomponents

import androidx.lifecycle.LifecycleOwner
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ioc.ToDoFragmentComponent
import com.example.todo.ui.view.controllers.HomeViewController

class HomeFragmentViewComponent(
    fragmentComponent: ToDoFragmentComponent,
    root: FragmentHomeBinding,
    viewLifecycleOwner: LifecycleOwner,
) {
    val taskViewController = HomeViewController(
        fragmentComponent.fragment,
        root,
        fragmentComponent.adapter,
        viewLifecycleOwner,
        fragmentComponent.viewModel,
    )
}