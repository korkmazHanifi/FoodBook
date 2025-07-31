package com.hanifikorkmaz.foodbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.hanifikorkmaz.foodbook.databinding.FragmentHomePageBinding
import androidx.navigation.findNavController


class HomePage : Fragment() {

    private var _binding: FragmentHomePageBinding? = null

    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { NewFood(it) }

    }

    fun NewFood(view: View){

        val action= HomePageDirections.actionHomePageToDetailPage("new",0)
        view.findNavController().navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}