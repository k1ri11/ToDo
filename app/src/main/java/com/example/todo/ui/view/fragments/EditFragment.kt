package com.example.todo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.todo.databinding.FragmentEditBinding
import com.example.todo.ioc.di.fragments.EditFragmentComponent
import com.example.todo.ioc.di.viewcomponents.EditViewComponent
import com.example.todo.ui.view.MainActivity
import com.example.todo.ui.view.controllers.EditViewController


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var editFragmentComponent: EditFragmentComponent
    private lateinit var editViewComponent: EditViewComponent
    private lateinit var editViewController: EditViewController

    private val args by navArgs<EditFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editFragmentComponent = (activity as MainActivity).activityComponent.editFragmentComponent().create(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        editViewComponent = editFragmentComponent.editViewComponent().create(
            binding,
            viewLifecycleOwner
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editViewController = editViewComponent.editViewController
        editViewController.loadTaskAndSetupUI(taskId = args.taskId, args.isNew)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editViewController.cancelGetSingleTaskJob()
        _binding = null
    }
}