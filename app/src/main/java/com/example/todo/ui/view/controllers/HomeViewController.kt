package com.example.todo.ui.view.controllers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.viewcomponents.HomeFragmentViewScope
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.NetworkUtils
import com.example.todo.ui.view.adapter.SwipeToDeleteCallback
import com.example.todo.ui.view.adapter.TaskAdapter
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
    private var animationNeeded = false
    private var doneCnt = 0


    fun setupViews() {
        setupNetworkState()
        setupFilteredTasksObserver()
        setupAllTasksObserver()
        setupNetworkObserver()

        setupFABListener()
        setupRefreshListener()

        setupVisibilitySelector()
        setupRecycler()
        createSwipeToDeleteCallback()
    }

    private fun createSwipeToDeleteCallback() {
        val simpleCallback = object : SwipeToDeleteCallback(fragment.requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedTask = adapter.toDoList[viewHolder.absoluteAdapterPosition]
                viewModel.deleteTask(deletedTask, connectionState)

                val str = buildSpannableString(deletedTask)
                makeSnackbar(str, deletedTask)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.tasksRv)
    }

    private fun makeSnackbar(str: SpannableStringBuilder, deletedTask: ToDoItem) {
        Snackbar.make(binding.refresh, str, Snackbar.LENGTH_LONG)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setTextMaxLines(1)
            .setAction(fragment.resources.getString(R.string.cancel)) {
                viewModel.addTask(deletedTask, connectionState)
            }.show()
    }

    private fun buildSpannableString(deletedTask: ToDoItem): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append(" ")
        builder.setSpan(ImageSpan(fragment.requireContext(), R.drawable.ic_5),
            builder.length - 1, builder.length, 0)
        builder.append(fragment.resources.getString(R.string.delete).plus(deletedTask.text))
        return builder
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
            Snackbar.make(binding.refresh,
                fragment.resources.getString(R.string.no_internet_connection),
                Snackbar.LENGTH_LONG)
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

            setAnimationStateAndStart()
            doneCnt = tasksList.count { it.done }
            binding.taskCnt.text = doneCnt.toString()
        }
    }

    private fun setAnimationStateAndStart() {
        if (tasksList.isEmpty()) {
            animationNeeded = true
            startAnimation()
        } else animationNeeded = false
    }

    private fun stopAnimation() {
        binding.fab.animate()
            .setListener(null)
            .cancel()
    }

    private fun startAnimation() {
        binding.fab.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    reverseAnimation()
                }
            })
            .setDuration(2000)
            .start()
    }

    private fun reverseAnimation() {
        binding.fab.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (animationNeeded) startAnimation()
                    else stopAnimation()
                }
            })
            .setDuration(2000)
            .start()
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