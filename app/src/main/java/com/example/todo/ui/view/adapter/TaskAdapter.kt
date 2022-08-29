package com.example.todo.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TodoItemBinding
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.fragments.HomeFragmentScope
import com.example.todo.ui.stateholders.ToDoViewModel
import javax.inject.Inject

@HomeFragmentScope
class TaskAdapter @Inject constructor(
    private val viewModel: ToDoViewModel,
    private val fragment: Fragment,
) : RecyclerView.Adapter<TaskViewHolder>() {

    var toDoList: List<ToDoItem> = emptyList()
        set(newValue) {
            val diffCallback = DiffUtilTaskCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding, viewModel, fragment)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = toDoList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

}