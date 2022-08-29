package com.example.todo.ioc.di

import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ioc.di.fragments.EditFragmentComponent
import com.example.todo.ioc.di.fragments.HomeFragmentComponent
import com.example.todo.ui.stateholders.ToDoViewModel
import dagger.BindsInstance
import dagger.Subcomponent

annotation class ActivityScope

@Subcomponent
@ActivityScope
interface ActivityComponent {
    var viewModel: ToDoViewModel

    fun homeFragmentComponent(): HomeFragmentComponent.Factory
    fun editFragmentComponent(): EditFragmentComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance viewModel: ToDoViewModel
            ):ActivityComponent
    }
}
