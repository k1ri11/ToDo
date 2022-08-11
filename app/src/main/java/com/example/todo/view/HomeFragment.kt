package com.example.todo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.model.ToDoItem
import com.example.todo.viewmodel.ToDoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment(), TaskAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ToDoViewModel
    private val adapter = TaskAdapter(this)
    private var doneCnt = 0

    private var filteredTasks = mutableListOf<ToDoItem>()
    private var tasksList = mutableListOf<ToDoItem>()

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
        val getAllTasksJob = viewModel.getAllTasks()

        viewModel.allTasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer { tasks ->
            tasksList = tasks.toMutableList()
            filteredTasks = tasks.filter { !it.done }.toMutableList()
            if (viewModel.doneIsInvisible) adapter.toDoList = filteredTasks
            else adapter.toDoList = tasksList

            doneCnt = tasksList.count { it.done }
            binding.taskCnt.text = doneCnt.toString()
        })

        binding.fab.setOnClickListener {
            getAllTasksJob.cancel()
            saveTaskList(tasksList)
            val action = HomeFragmentDirections.homeFrToEditFr(UUID.randomUUID(), isNew = true)
            findNavController().navigate(action)
        }

        setupUI()
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
        Log.d("TAG", "onViewCreated: visibility ${viewModel.doneIsInvisible}")
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
        saveTaskList(tasksList)
        findNavController().navigate(HomeFragmentDirections.homeFrToEditFr(taskId, isNew = false))
    }

    private fun saveTaskList(tasksList: List<ToDoItem>) {
        viewModel.saveTaskList(tasksList)
    }

    override fun onCheckBoxClick(position: Int) {
        val currentItem = adapter.toDoList[position]
        if (currentItem.done) doneCnt -= 1
        else doneCnt += 1
        binding.taskCnt.text = doneCnt.toString()

        val index = tasksList.indexOf(currentItem)
        tasksList[index].done = !tasksList[index].done
        filteredTasks = tasksList.filter { !it.done }.toMutableList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}