package com.example.todo.ui.view.controllers

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.viewcomponents.FragmentViewScope
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.MainActivity
import com.example.todo.ui.view.TaskAdapter
import com.example.todo.ui.view.fragments.HomeFragmentDirections
import com.example.todo.utils.Resource
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject

@FragmentViewScope
class HomeViewController @Inject constructor(
    private val fragment: Fragment,
    private val binding: FragmentHomeBinding,
    private val adapter: TaskAdapter,
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: ToDoViewModel,
) {

    private var filteredTasks = emptyList<ToDoItem>()
    private var tasksList = emptyList<ToDoItem>()
    private var getAllTasksJob: Job? = null
    private val activity = fragment.requireActivity() as MainActivity

    private var isRefreshing = false
    private var doneCnt = 0


    fun setupViews() {
        getAllTasks()
        setupFilteredTasksObserver()
        setupAllTasksObserver()

        setupFABListener()
        setupRefreshListener()

        setupVisibilitySelector()
        setupRecycler()
    }

    private fun getAllTasks() {
        if (activity.isConnected) getAllTasksJob = viewModel.getAllTasks()
        else {
            changeLoadingState(false)
            Toast.makeText(fragment.requireContext(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()}
    }

    private fun setupVisibilitySelector() {
        setVisibilityAndListInRecycler(tasksList)
        binding.visibilitySelector.setOnClickListener {
            viewModel.doneIsInvisible = !viewModel.doneIsInvisible
            setVisibilityAndListInRecycler(tasksList)
        }
    }

    private fun setupRecycler() {
        binding.apply {
            tasksRv.adapter = adapter
            tasksRv.layoutManager = LinearLayoutManager(fragment.requireActivity())
        }
    }

    private fun setupRefreshListener() {
        binding.refresh.setOnRefreshListener {
            if (!isRefreshing) {
                getAllTasks()
            }
        }
    }

    private fun setupFABListener() {
        binding.fab.setOnClickListener {
            val action = HomeFragmentDirections.homeFrToEditFr(UUID.randomUUID(), isNew = true)
            findNavController(fragment).navigate(action)
        }
    }

    private fun setupFilteredTasksObserver() {
        viewModel.filteredTasks.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    filteredTasks = response.data!!
                }
                is Resource.Error -> {
                    response.message?.let { message -> Toast.makeText(fragment.requireContext(),
                        "Ошибка: $message", Toast.LENGTH_LONG).show()
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupAllTasksObserver() {
        viewModel.allTasks.observe(viewLifecycleOwner) { response ->
            handleAllTasksResponse(response)
        }
    }

    private fun handleAllTasksResponse(response: Resource<List<ToDoItem>>) {
        when (response) {
            is Resource.Success -> {
                handleSuccessResponse(response)
            }
            is Resource.Error -> {
                changeLoadingState(false)
                response.message?.let { message ->
                    Toast.makeText(fragment.requireContext(),
                        "Ошибка: $message проведите вниз, чтобы обновить", Toast.LENGTH_LONG).show()
                }
            }
            is Resource.Loading -> {
                changeLoadingState(true)
            }
        }
    }

    private fun changeLoadingState(state: Boolean) {
        binding.refresh.isRefreshing = state
        isRefreshing = state
    }

    private fun handleSuccessResponse(response: Resource.Success<List<ToDoItem>>) {
        response.data?.let {
            changeLoadingState(false)
            tasksList = it
            if (viewModel.doneIsInvisible) adapter.toDoList = filteredTasks
            else adapter.toDoList = tasksList

            doneCnt = tasksList.count { it.done }
            binding.taskCnt.text = doneCnt.toString()
        }
    }

    private fun setVisibilityAndListInRecycler(tasks: List<ToDoItem>) {
        if (viewModel.doneIsInvisible) {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_off)
            adapter.toDoList = filteredTasks
        } else {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_on)
            adapter.toDoList = tasks
        }
    }

    fun saveTaskList() {
        viewModel.saveTaskList(tasksList)
    }

    fun cancelGetAllTasksJob() {
        getAllTasksJob?.cancel()
    }
}