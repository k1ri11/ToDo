package com.example.todo.ioc.di

import com.example.todo.ioc.di.fragments.EditFragmentComponent
import com.example.todo.ioc.di.fragments.HomeFragmentComponent
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.NetworkUtilsImpl
import com.example.todo.ui.view.fragments.EditFragment
import com.example.todo.ui.view.fragments.HomeFragment
import dagger.BindsInstance
import dagger.Subcomponent

annotation class ActivityScope

@Subcomponent
@ActivityScope
interface ActivityComponent {
    var viewModel: ToDoViewModel
    val networkUtils: NetworkUtilsImpl

    fun homeFragmentComponent(): HomeFragmentComponent.Factory
    fun editFragmentComponent(): EditFragmentComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance viewModel: ToDoViewModel
            ):ActivityComponent
    }
}
