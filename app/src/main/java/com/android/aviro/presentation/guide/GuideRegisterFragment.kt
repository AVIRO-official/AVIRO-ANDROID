package com.android.aviro.presentation.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentGuideRegisterBinding

class GuideRegisterFragment : Fragment() {

    private var _binding: FragmentGuideRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : GuideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGuideRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = GuideViewModel("register")


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}