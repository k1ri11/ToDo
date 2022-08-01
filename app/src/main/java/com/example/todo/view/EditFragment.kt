package com.example.todo.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.model.Importance
import com.example.todo.model.ToDoItem
import com.example.todo.viewmodel.ToDoViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.*


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ToDoViewModel
    private lateinit var task: ToDoItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.singleTask.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            task = it
            setupUI()
        })

        val toolbar = binding.toolbar
        val popupMenu = PopupMenu(requireContext(), binding.importanceSection)
        popupMenu.inflate(R.menu.popup_menu)

        setupListeners(toolbar, popupMenu)
    }


    private fun setupUI() {
        if (task.text.isNotEmpty()) binding.editText.setText(
            task.text,
            TextView.BufferType.EDITABLE
        )
        when (task.importance) {
            is Importance.Low -> binding.importance.text = "Низкий"
            is Importance.High -> {
                binding.importance.text = "Высокий"
                binding.importance.setTextColor(resources.getColor(R.color.Red, null))
                binding.importanceIc.visibility = View.VISIBLE
            }
            is Importance.Basic -> binding.importance.text = "Нет"
        }
        task.deadLine?.let {
            binding.swich.isChecked = true
            binding.deadlineDate.text = dateToString(it)
            binding.deadlineDate.visibility = View.VISIBLE
        }
    }

    private fun setupListeners(toolbar: MaterialToolbar, popupMenu: PopupMenu) {
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.save) {
                task.text = binding.editText.text.toString()
                task.changed_at = Date(System.currentTimeMillis())
                Log.d("TAG", "setupListeners: task $task")
                viewModel.saveTask(task)
                findNavController().navigate(R.id.editFr_to_homeFr)
            }
            true
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.editFr_to_homeFr)
        }

        binding.deleteSection.setOnClickListener {
            viewModel.deleteTask(task.id)
            findNavController().navigate(R.id.editFr_to_homeFr)
        }

        setupSwichListener()
        setupPopupListener(popupMenu)
    }

    private fun setupSwichListener() {
        binding.apply {
            swich.setOnClickListener {
                if (swich.isChecked) {
                    deadlineDate.text = "Выберите дату"
                    deadlineDate.visibility = View.VISIBLE
                } else {
                    deadlineDate.visibility = View.INVISIBLE
                    task.deadLine = null
                }
            }
        }
        setupDatePicker()
    }

    private fun setupPopupListener(popupMenu: PopupMenu) {
        binding.importanceSection.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.imp_no -> {
                    binding.importance.setTextColor(resources.getColor(R.color.black, null))
                    binding.importanceIc.visibility = View.GONE
                    binding.importance.text = "Нет"
                    task.importance = Importance.Basic
                }
                R.id.imp_low -> {
                    binding.importance.setTextColor(resources.getColor(R.color.black, null))
                    binding.importanceIc.visibility = View.GONE
                    binding.importance.text = "Низкий"
                    task.importance = Importance.Low
                }
                R.id.imp_high -> {
                    binding.importance.setTextColor(resources.getColor(R.color.Red, null))
                    binding.importanceIc.visibility = View.VISIBLE
                    binding.importance.text = "Высокий"
                    task.importance = Importance.High
                }
            }
            false
        }
    }

    private fun setupDatePicker() {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.deadlineDate.text = dateToString(cal.time)
                task.deadLine = cal.time
            }

        binding.deadlineDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun dateToString(date: Date): String {
        val format = "dd.MM.yyyy"
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}