package com.example.todo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.TodoItemBinding
import com.example.todo.model.Importance
import com.example.todo.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.*

class DiffUtilTaskCallback(
    private var oldList: List<ToDoItem>,
    private var newList: List<ToDoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }
}

class TaskAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var toDoList: List<ToDoItem> = emptyList()
        set(newValue) {
            val diffCallback = DiffUtilTaskCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    inner class TaskViewHolder(val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = toDoList[position]
        holder.binding.apply {
            itemCb.isChecked = currentItem.done
            when (currentItem.importance) {
                Importance.Low -> {
                    itemImportance.setImageResource(R.drawable.ic_low_imp)
                    itemImportance.visibility = View.VISIBLE
                }
                Importance.High -> {
                    itemImportance.setImageResource(R.drawable.ic_high_imp)
                    itemImportance.visibility = View.VISIBLE
                }
                Importance.Basic -> itemImportance.visibility = View.GONE
            }
            itemText.text = currentItem.text
            if (currentItem.deadLine == null) {
                itemDate.visibility = View.GONE
            } else {
                val format = "dd.MM.yyyy"
                val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                itemDate.text = dateFormat.format(currentItem.deadLine!!)
                itemDate.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }
}