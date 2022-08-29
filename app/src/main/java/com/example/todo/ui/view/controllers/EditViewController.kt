package com.example.todo.ui.view.controllers

import android.app.DatePickerDialog
import android.util.TypedValue
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.todo.R
import com.example.todo.data.model.Importance
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.domain.model.ToDoItem
import com.example.todo.ioc.di.viewcomponents.EditFragmentViewScope
import com.example.todo.ui.stateholders.ToDoViewModel
import com.example.todo.ui.view.fragments.EditFragment
import com.example.todo.utils.Resource
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@EditFragmentViewScope
class EditViewController @Inject constructor(
    private val fragment: Fragment,
    private val binding: FragmentEditBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: ToDoViewModel,
) {

    private lateinit var task: ToDoItem
    var getTaskJob: Job? = null
    private var isLoading = false
    private var isNewTask = false
    private var connectionState = false

    fun loadTaskAndSetupUI(taskId: UUID, isNew: Boolean) {
        isNewTask = isNew
        setupNetworkObserver()

        if (isNewTask) viewModel.createEmptyTask(taskId)
        else getTask(taskId)

        val toolbar = binding.toolbar
        val popupMenu = PopupMenu(fragment.requireContext(), binding.importanceSection)
        popupMenu.inflate(R.menu.popup_menu)

        setupSingleTaskObserver(toolbar, popupMenu)
        setupToolBarNavigation(toolbar)
    }

    private fun setupNetworkObserver() {
        val networkUtils = (fragment as EditFragment).networkUtils
        connectionState = networkUtils.hasInternetConnection()
        networkUtils.getNetworkLiveData().observe(viewLifecycleOwner) { isConnected ->
            connectionState = isConnected
        }
    }

    private fun getTask(taskId: UUID) {
        getTaskJob = viewModel.getTask(taskId, connectionState)
        changeLoadingState(false)
        if (!connectionState) {
            Snackbar.make(binding.deleteSection, fragment.resources.getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show()
        }
    }

    private fun setupToolBarNavigation(toolbar: MaterialToolbar) {
        toolbar.setNavigationOnClickListener {
            findNavController(fragment).navigate(R.id.editFr_to_homeFr)
        }
    }

    private fun setupSingleTaskObserver(toolbar: MaterialToolbar, popupMenu: PopupMenu) {
        viewModel.singleTask.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    handleSuccessResponse(response)
                    setupListeners(toolbar, popupMenu)
                    updateUI()
                }
                is Resource.Error -> {
                    changeLoadingState(false)
                    response.message?.let { message ->
                        Toast.makeText(fragment.requireContext(),
                            fragment.resources.getString(R.string.error).plus(message),
                            Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    changeLoadingState(true)
                }
            }
        }
    }

    private fun handleSuccessResponse(response: Resource.Success<ToDoItem>) {
        changeLoadingState(false)
        response.data?.let {
            task = it
        }
    }

    private fun changeLoadingState(state: Boolean) {
        isLoading = state
        if (state) binding.prBar.visibility = View.VISIBLE
        else binding.prBar.visibility = View.GONE
    }

    private fun updateUI() {
        binding.editText.setText(task.text, TextView.BufferType.EDITABLE)
        if (!isNewTask) binding.deleteSection.visibility = View.VISIBLE

        updateImportanceSection()
        updateDeadlineSection()
    }

    private fun updateDeadlineSection() {
        val deadLine = task.deadLine
        if (deadLine != null) {
            binding.swich.isChecked = true
            binding.deadlineDate.text = dateToString(deadLine)
            binding.deadlineDate.visibility = View.VISIBLE
        } else {
            binding.swich.isChecked = false
            binding.deadlineDate.text = ""
            binding.deadlineDate.visibility = View.INVISIBLE
        }
    }

    private fun updateImportanceSection() {
        when (task.importance) {
            is Importance.Low -> {
                setLowImportance()
            }
            is Importance.High -> {
                setHighImportance()
            }
            is Importance.Basic -> {
                setNoImportance()
            }
        }
    }

    private fun getTextColor(): Int {
        val typedValue = TypedValue()
        fragment.requireContext().theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        return ContextCompat.getColor(fragment.requireContext(), typedValue.resourceId)
    }

    private fun setupListeners(toolbar: MaterialToolbar, popupMenu: PopupMenu) {
        setupSaveMenuItemListener(toolbar)
        setupSwichListener()
        setupPopupListener(popupMenu)
        setupDatePicker()
        setupDeleteListener()
    }

    private fun setupDeleteListener() {
        binding.deleteSection.setOnClickListener {
            deleteTask()
            findNavController(fragment).navigate(R.id.editFr_to_homeFr)
        }
    }

    private fun setupSaveMenuItemListener(toolbar: MaterialToolbar) {
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.save) {
                onSaveClick()
            }
            true
        }
    }

    private fun onSaveClick() {
        val inputText = binding.editText.text.toString()
        if (inputText.isNotBlank()) {
            task.text = inputText
            task.changedAt = Date(System.currentTimeMillis())
            saveTask()
            findNavController(fragment).navigate(R.id.editFr_to_homeFr)
        } else {
            Toast.makeText(fragment.requireContext(), fragment.resources.getString(R.string.enter_task_text),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSwichListener() {
        binding.swich.setOnClickListener {
            if (binding.swich.isChecked) {
                binding.deadlineDate.text =  fragment.resources.getString(R.string.enter_date)
                binding.deadlineDate.visibility = View.VISIBLE
            } else {
                binding.deadlineDate.visibility = View.INVISIBLE
                task.deadLine = null
            }
        }
    }

    private fun setupPopupListener(popupMenu: PopupMenu) {
        binding.importanceSection.setOnClickListener {
            popupMenu.show()
        }
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.imp_no -> {
                    setNoImportance()
                    task.importance = Importance.Basic
                }
                R.id.imp_low -> {
                    setLowImportance()
                    task.importance = Importance.Low
                }
                R.id.imp_high -> {
                    setHighImportance()
                    task.importance = Importance.High
                }
            }
            false
        }
    }

    private fun setHighImportance() {
        binding.importance.setTextColor(fragment.requireContext().getColor(R.color.Red))
        binding.importanceIc.visibility = View.VISIBLE
        binding.importance.text = fragment.requireContext().getString(R.string.high)
    }

    private fun setLowImportance() {
        binding.importance.setTextColor(getTextColor())
        binding.importanceIc.visibility = View.GONE
        binding.importance.text = fragment.requireContext().getString(R.string.low)
    }

    private fun setNoImportance() {
        binding.importance.setTextColor(getTextColor())
        binding.importanceIc.visibility = View.GONE
        binding.importance.text = fragment.requireContext().getString(R.string.No)
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.deadlineDate.text = dateToString(calendar.time)
                task.deadLine = calendar.time
            }
        binding.deadlineDate.setOnClickListener {
            DatePickerDialog(
                fragment.requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun dateToString(date: Date): String {
        val format = "dd.MM.yyyy"
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun saveTask() {
        if (isNewTask) viewModel.addTask(task, connectionState)
        else viewModel.updateTask(task, connectionState)
    }

    fun cancelGetSingleTaskJob() {
        getTaskJob?.cancel()
    }

    private fun deleteTask() {
        viewModel.deleteTask(task, connectionState)
    }
}