package com.example.todo.ioc.di.viewcomponents

import androidx.lifecycle.LifecycleOwner
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ui.view.controllers.HomeViewController
import dagger.BindsInstance
import dagger.Subcomponent

annotation class HomeFragmentViewScope

@HomeFragmentViewScope
@Subcomponent
interface HomeViewComponent {
    val root: FragmentHomeBinding
    val viewLifecycleOwner: LifecycleOwner
    val homeViewController: HomeViewController

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance root: FragmentHomeBinding,
            @BindsInstance viewLifecycleOwner: LifecycleOwner,
        ): HomeViewComponent
    }
}