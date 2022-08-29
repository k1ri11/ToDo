package com.example.todo.ioc.di.fragments

import androidx.fragment.app.Fragment
import com.example.todo.ioc.di.viewcomponents.EditViewComponent
import com.example.todo.ioc.di.viewcomponents.HomeViewComponent
import com.example.todo.ui.view.TaskAdapter
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface EditFragmentComponent {
    val fragment: Fragment

    fun editViewComponent(): EditViewComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): EditFragmentComponent
    }
}