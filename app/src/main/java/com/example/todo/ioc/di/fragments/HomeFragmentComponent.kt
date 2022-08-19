package com.example.todo.ioc.di.fragments

import androidx.fragment.app.Fragment
import com.example.todo.ioc.di.viewcomponents.HomeViewComponent
import com.example.todo.ui.view.TaskAdapter
import dagger.BindsInstance
import dagger.Subcomponent

annotation class HomeFragmentScope

@Subcomponent
@HomeFragmentScope
interface HomeFragmentComponent {
    val fragment: Fragment
    val adapter: TaskAdapter

    fun homeViewComponentComponent(): HomeViewComponent.Factory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): HomeFragmentComponent
    }
}