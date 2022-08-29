package com.example.todo.ui.view

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.Importance
import com.example.todo.databinding.TodoItemBinding
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.fragments.HomeFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(
    private val binding: TodoItemBinding,
    private val viewModel: ToDoViewModel,
    private val fragment: Fragment,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(currentItem: ToDoItem) {
        binding.apply {
            setupOnCheckBoxClickListener(currentItem)
            setupOnItemClickListener(currentItem)

            itemCb.isChecked = currentItem.done
            itemText.text = currentItem.text
            bindImportance(currentItem.importance)
            bindDeadLine(currentItem.deadLine)
        }
    }

    private fun setupOnCheckBoxClickListener(currentItem: ToDoItem) {
        binding.itemCb.setOnClickListener {
            viewModel.changeItemDone(currentItem)
        }
    }

    private fun setupOnItemClickListener(currentItem: ToDoItem) {
        binding.itemTextWrapper.setOnClickListener {
            val taskId = currentItem.id
            val action = HomeFragmentDirections.homeFrToEditFr(taskId, isNew = false)
            findNavController(fragment).navigate(action)
        }
    }

    private fun bindDeadLine(deadLine: Date?) {
        if (deadLine == null) {
            binding.itemDate.visibility = View.GONE
        } else {
            val format = "dd.MM.yyyy"
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            binding.itemDate.text = dateFormat.format(deadLine)
            binding.itemDate.visibility = View.VISIBLE
        }
    }

    private fun bindImportance(importance: Importance) {
        when (importance) {
            Importance.Low -> {
                binding.itemImportance.setImageResource(R.drawable.ic_low_imp)
                binding.itemImportance.visibility = View.VISIBLE
            }
            Importance.High -> {
                binding.itemImportance.setImageResource(R.drawable.ic_high_imp)
                binding.itemImportance.visibility = View.VISIBLE
            }
            Importance.Basic -> binding.itemImportance.visibility = View.GONE
        }
    }
}