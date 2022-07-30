package com.example.todo.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.model.ToDoItem
import com.example.todo.viewmodel.ToDoViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.*


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ToDoViewModel

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
        val task = viewModel.singleTask.value!!
        setupUI(task)

        val toolbar = binding.toolbar
        val popupMenu = PopupMenu(requireContext(), binding.importanceSection)
        popupMenu.inflate(R.menu.popup_menu)

        setupListeners(toolbar, popupMenu, task)
    }


    private fun setupUI(task: ToDoItem) {
        if (task.text.isNotEmpty()) binding.editText.setText(
            task.text,
            TextView.BufferType.EDITABLE
        )
        when (task.importance) {
            "low" -> binding.importance.text = "Низкий"
            "high" -> {
                binding.importance.text = "Высокий"
                binding.importance.setTextColor(resources.getColor(R.color.Red, null))
                binding.importanceIc.visibility = View.VISIBLE
            }
            "basic" -> binding.importance.text = "Нет"
        }
        if (task.deadLine.isNotEmpty()) {
            binding.swich.isChecked = true
            binding.deadlineDate.text = task.deadLine
            binding.deadlineDate.visibility = View.VISIBLE
        }
    }

    private fun setupListeners(toolbar: MaterialToolbar, popupMenu: PopupMenu, task: ToDoItem) {
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.save) findNavController().navigate(R.id.editFr_to_homeFr)
            true
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.editFr_to_homeFr)
        }

        binding.deleteSection.setOnClickListener {
            //todo delete
            findNavController().navigate(R.id.editFr_to_homeFr)
        }

        setupSwichListener(task)
        setupPopupListener(popupMenu, task)
    }

    private fun setupSwichListener(task: ToDoItem) {
        binding.apply{
            swich.setOnClickListener {
                if (swich.isChecked) {
                    deadlineDate.text = "Выберите дату"
                    deadlineDate.visibility = View.VISIBLE
                } else {
                    deadlineDate.visibility = View.GONE
                }
            }
        }
        setupDatePicker(task)
    }

    private fun setupPopupListener(popupMenu: PopupMenu, task: ToDoItem) {
        binding.importanceSection.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.imp_no -> {
                    binding.importance.setTextColor(resources.getColor(R.color.black, null))
                    binding.importanceIc.visibility = View.GONE
                    binding.importance.text = "Нет"
                    task.importance = "basic"
                }
                R.id.imp_low -> {
                    binding.importance.setTextColor(resources.getColor(R.color.black, null))
                    binding.importanceIc.visibility = View.GONE
                    binding.importance.text = "Низкий"
                    task.importance = "low"
                }
                R.id.imp_high -> {
                    binding.importance.setTextColor(resources.getColor(R.color.Red, null))
                    binding.importanceIc.visibility = View.VISIBLE
                    binding.importance.text = "Высокий"
                    task.importance = "high"
                }
            }
            false
        }
    }

    private fun setupDatePicker(task: ToDoItem) {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val format = "dd.MM.yyyy"
                val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                val curDeadline = dateFormat.format(cal.time)
                binding.deadlineDate.text = curDeadline
                task.deadLine = curDeadline
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}