package com.example.todo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.ioc.di.fragments.HomeFragmentComponent
import com.example.todo.ioc.di.viewcomponents.HomeViewComponent
import com.example.todo.ui.view.MainActivity
import com.example.todo.ui.view.NetworkUtils
import com.example.todo.ui.view.controllers.HomeViewController
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeFragmentComponent: HomeFragmentComponent
    private lateinit var homeViewComponent: HomeViewComponent
    private lateinit var homeViewController: HomeViewController
    @Inject
    lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityComponent = (activity as MainActivity).activityComponent
        homeFragmentComponent = activityComponent.homeFragmentComponent().create(this)
        activityComponent.inject(this)
        networkUtils = NetworkUtils(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewComponent = homeFragmentComponent.homeViewComponentComponent().create(
            root = binding,
            viewLifecycleOwner = viewLifecycleOwner
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewController = homeViewComponent.homeViewController
        homeViewController.setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewController.cancelGetAllTasksJob()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        homeViewController.saveTaskList()
    }
}