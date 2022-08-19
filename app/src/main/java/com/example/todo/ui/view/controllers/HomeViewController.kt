package com.example.todo.ui.view.controllers

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.viewcomponents.HomeFragmentViewScope
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.NetworkUtils
import com.example.todo.ui.view.TaskAdapter
import com.example.todo.ui.view.fragments.HomeFragment
import com.example.todo.ui.view.fragments.HomeFragmentDirections
import com.example.todo.utils.Resource
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import java.util.*
import javax.inject.Inject

@HomeFragmentViewScope
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
    private var connectionState = false
    private lateinit var networkUtils: NetworkUtils

    private var isRefreshing = false
    private var doneCnt = 0


    fun setupViews() {
        setupNetworkState()
        getAllTasks()
        setupFilteredTasksObserver()
        setupAllTasksObserver()
        setupNetworkObserver()

        setupFABListener()
        setupRefreshListener()

        setupVisibilitySelector()
        setupRecycler()
    }

    private fun setupNetworkState() {
        networkUtils = (fragment as HomeFragment).networkUtils
        connectionState = networkUtils.hasInternetConnection()
    }

    private fun setupNetworkObserver() {
        networkUtils = (fragment as HomeFragment).networkUtils
        networkUtils.getNetworkLiveData().observe(viewLifecycleOwner) { isConnected ->
            connectionState = isConnected
            if (isConnected && tasksList.isNotEmpty()) {
                saveTaskList()
            }
        }
    }

    private fun getAllTasks() {
        getAllTasksJob = viewModel.getAllTasks(connectionState)
        changeLoadingState(false)
        if (!connectionState) {
            Snackbar.make(binding.refresh, fragment.resources.getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show()
        }
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
                    response.message?.let { message ->
                        Toast.makeText(fragment.requireContext(),
                            fragment.resources.getString(R.string.error).plus(message),
                            Toast.LENGTH_LONG).show()
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
                        fragment.resources.getString(R.string.error).plus(message)
                            .plus(R.string.update), Toast.LENGTH_LONG).show()
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
        viewModel.saveTaskList(tasksList, connectionState)
    }

    fun cancelGetAllTasksJob() {
        getAllTasksJob?.cancel()
    }
}