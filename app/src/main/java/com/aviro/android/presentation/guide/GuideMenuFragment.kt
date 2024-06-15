package com.aviro.android.presentation.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.databinding.FragmentGuideMenuBinding

class GuideMenuFragment : Fragment() {

    private val sharedViewModel: GuideViewModel by activityViewModels()

    private var _binding: FragmentGuideMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel : GuideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGuideMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}