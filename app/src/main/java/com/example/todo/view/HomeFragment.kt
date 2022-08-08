package com.example.todo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.data.item.ToDoItem
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.utils.Resource
import com.example.todo.viewmodel.ToDoViewModel
import kotlinx.coroutines.Job
import java.util.*

class HomeFragment : Fragment(), TaskAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel
    private val adapter = TaskAdapter(this)
    private var doneCnt = 0
    var isRefreshing = false

    private var filteredTasks = emptyList<ToDoItem>()
    private var tasksList = emptyList<ToDoItem>()
    private var getAllTasksJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllTasksJob = viewModel.getAllTasks()
        viewModel.filteredTasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        filteredTasks = response.data!!
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Toast.makeText(activity, "Ошибка: $message", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Loading -> {
                    }
                }
        })

        viewModel.allTasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            handleAllTasksResponse(response)
        })

        binding.fab.setOnClickListener {
            getAllTasksJob?.cancel()
            val action = HomeFragmentDirections.homeFrToEditFr(UUID.randomUUID(), isNew = true)
            findNavController().navigate(action)
        }

        binding.refresh.setOnRefreshListener {
            if (!isRefreshing){
                getAllTasksJob = viewModel.getAllTasks()
            }
        }

        setupUI()
    }

    private fun handleAllTasksResponse(response: Resource<List<ToDoItem>>) {
        when (response) {
            is Resource.Success -> {
                response.data?.let {
                    tasksList = it
                    if (viewModel.doneIsInvisible) adapter.toDoList = filteredTasks
                    else adapter.toDoList = tasksList

                    doneCnt = tasksList.count { it.done }
                    binding.taskCnt.text = doneCnt.toString()
                    binding.refresh.isRefreshing = false
                    isRefreshing = false
                }
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    Toast.makeText(activity, "Ошибка: $message проведите вниз, чтобы обновить", Toast.LENGTH_LONG)
                        .show()
                    binding.refresh.isRefreshing = false
                    isRefreshing = false
                }
            }
            is Resource.Loading -> {
                binding.refresh.isRefreshing = true
                isRefreshing = true
            }
        }
    }

    private fun setupUI() {
        setVisibility(tasksList)

        binding.apply {
            tasksRv.adapter = adapter
            tasksRv.layoutManager = LinearLayoutManager(activity)

            visibilitySelector.setOnClickListener {
                viewModel.doneIsInvisible = !viewModel.doneIsInvisible
                setVisibility(tasksList)
            }
        }
    }

    private fun setVisibility(tasks: List<ToDoItem>) {
        if (viewModel.doneIsInvisible) {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_off)
            adapter.toDoList = filteredTasks
        } else {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_on)
            adapter.toDoList = tasks
        }
    }

    override fun onItemClick(position: Int) {
        val currentItem = adapter.toDoList[position]
        val taskId = currentItem.id
        val action = HomeFragmentDirections.homeFrToEditFr(taskId, isNew = false)
        findNavController().navigate(action)
    }

    private fun saveTaskList(tasksList: List<ToDoItem>) {
        viewModel.saveTaskList(tasksList)
    }

    override fun onCheckBoxClick(position: Int) {
        val currentItem = adapter.toDoList[position]
        if (currentItem.done) doneCnt -= 1
        else doneCnt += 1
        binding.taskCnt.text = doneCnt.toString()

        viewModel.changeItemDone(currentItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        saveTaskList(tasksList)
    }

}