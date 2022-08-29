package com.example.todo.ioc.di.fragments

import androidx.fragment.app.Fragment
import com.example.todo.ioc.di.viewcomponents.EditViewComponent
import dagger.BindsInstance
import dagger.Subcomponent

annotation class EditFragmentScope

@Subcomponent
@EditFragmentScope
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