package com.example.todo.ioc.di.viewcomponents

import androidx.lifecycle.LifecycleOwner
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ui.view.controllers.EditViewController
import com.example.todo.ui.view.controllers.HomeViewController
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentViewScope
@Subcomponent
interface EditViewComponent {
    val root: FragmentEditBinding
    val viewLifecycleOwner: LifecycleOwner
    val editViewController: EditViewController

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance root: FragmentEditBinding,
            @BindsInstance viewLifecycleOwner: LifecycleOwner,
        ): EditViewComponent
    }
}
