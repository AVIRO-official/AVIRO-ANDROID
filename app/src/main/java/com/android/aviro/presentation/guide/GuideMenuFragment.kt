package com.android.aviro.presentation.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentGuideMenuBinding

class GuideMenuFragment : Fragment() {

    private var _binding: FragmentGuideMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : GuideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGuideMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = GuideViewModel("menu")


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}