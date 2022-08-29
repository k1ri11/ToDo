package com.example.todo.ui.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todo.ToDoApp
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ioc.ApplicationComponent
import com.example.todo.ioc.ToDoFragmentComponent
import com.example.todo.ioc.viewcomponents.HomeFragmentViewComponent
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.TaskAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    private val applicationComponent
//        get() = ToDoApp.get(requireContext()).applicationComponent
//    private lateinit var fragmentComponent: ToDoFragmentComponent
//    private var fragmentViewComponent: ToDoFragmentViewComponent? = null
//    private val viewModel: ToDoViewModel by viewModels { applicationComponent.viewModelFactory }
//    private val adapter = TaskAdapter(viewModel)

    private lateinit var applicationComponent: ApplicationComponent
    private lateinit var fragmentComponent: ToDoFragmentComponent
    private var fragmentViewComponent: HomeFragmentViewComponent? = null

    private lateinit var viewModel: ToDoViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = ToDoFragmentComponent (
             applicationComponent,
        fragment = this,
        viewModel = viewModel,
        )
        adapter = fragmentComponent.adapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationComponent = ToDoApp.get(context).applicationComponent
        val viewModel1: ToDoViewModel by viewModels { applicationComponent.viewModelFactory }
        viewModel = viewModel1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fragmentViewComponent = HomeFragmentViewComponent(
            fragmentComponent,
            root = binding,
            viewLifecycleOwner = viewLifecycleOwner,
        ).apply {
            taskViewController.setupViews()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent?.taskViewController?.cancelGetAllTasksJob()
        _binding = null
        fragmentViewComponent = null
    }

    override fun onPause() {
        super.onPause()
        fragmentViewComponent?.taskViewController?.saveTaskList()
    }

}