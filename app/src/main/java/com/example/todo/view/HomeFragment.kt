package com.example.todo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var doneIsVisible = true  //todo перекинуть в viewmodel
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
        var doneCnt = 0
        val tasks = viewModel.allTasks.value!!
        if (!tasks.isNullOrEmpty()) {
            doneCnt = tasks.count { it.done } //todo перекинуть donecnt в viewmodel
            adapter.toDoList = tasks
        }

        binding.apply {
            tasksRv.adapter = adapter
            tasksRv.layoutManager = LinearLayoutManager(activity)
            taskCnt.text = doneCnt.toString()
            fab.setOnClickListener {
                viewModel.createEmptyTask()
                findNavController().navigate(R.id.homeFr_to_editFr)
            }
            visibilitySelector.setOnClickListener {
                setupVisibilityListener(tasks)
            }
        }
    }

    private fun setupVisibilityListener(tasks: List<ToDoItem>) {
        if (doneIsVisible) {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_off)
            adapter.toDoList = tasks.filter { !it.done }
        } else {
            binding.visibilitySelector.setImageResource(R.drawable.ic_visibility_on)
            adapter.toDoList = tasks
        }
        doneIsVisible = !doneIsVisible
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