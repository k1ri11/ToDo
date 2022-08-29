package com.example.todo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todo.ToDoApp
import com.example.todo.domain.model.ToDoItem
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.ioc.ToDoFragmentComponent
import com.example.todo.ioc.viewcomponents.EditFragmentViewComponent
import com.example.todo.ui.stateholders.ToDoViewModel
import kotlinx.coroutines.Job


class EditFragment : Fragment() {

    private val applicationComponent
        get() = ToDoApp.get(requireContext()).applicationComponent
    private lateinit var fragmentComponent: ToDoFragmentComponent
    private var fragmentViewComponent: EditFragmentViewComponent? = null
    private val viewModel: ToDoViewModel by viewModels { applicationComponent.viewModelFactory }

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = ToDoFragmentComponent(
            applicationComponent,
            fragment = this,
            viewModel = viewModel,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        fragmentViewComponent = EditFragmentViewComponent(
            fragmentComponent,
            root = binding,
            viewLifecycleOwner = viewLifecycleOwner,
        ).apply {
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewComponent?.editViewController?.loadTaskAndSetupUI(args.taskId, args.isNew)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent?.editViewController?.cancelGetSingleTaskJob()
        _binding = null
        fragmentViewComponent = null
    }

}