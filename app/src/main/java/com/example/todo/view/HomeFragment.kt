package com.example.todo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.model.ToDoItem
import com.example.todo.viewmodel.ToDoViewModel

class HomeFragment : Fragment(), TaskAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ToDoViewModel
    private val adapter = TaskAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllTasks()
        var tasksList = emptyList<ToDoItem>()
        var doneCnt = 0
        viewModel.allTasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer { tasks ->
            tasksList = tasks
            doneCnt = tasksList.count { it.done }
            binding.taskCnt.text = doneCnt.toString()
            adapter.toDoList = tasksList
        })

        binding.apply {
            tasksRv.adapter = adapter
            tasksRv.layoutManager = LinearLayoutManager(activity)

            fab.setOnClickListener {
                viewModel.createEmptyTask()
                findNavController().navigate(R.id.homeFr_to_editFr)
            }
            visibilitySelector.setOnClickListener {
                setupVisibilityListener(tasksList)
            }
        }
    }

    private fun setupVisibilityListener(tasks: List<ToDoItem>) {
        if (viewModel.doneVisibility) {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_off)
            adapter.toDoList = tasks.filter { !it.done }
        } else {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_on)
            adapter.toDoList = tasks
        }
        viewModel.doneVisibility = !viewModel.doneVisibility
    }

    override fun onItemClick(position: Int) {
        val currentItem = adapter.toDoList[position]
        viewModel.getSingleTask(currentItem.id)
        findNavController().navigate(R.id.homeFr_to_editFr)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}