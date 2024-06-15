package com.aviro.android.presentation.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.databinding.FragmentUpdateNumberBinding

class UpdateNumberFragment : Fragment() {

    private var _binding: FragmentUpdateNumberBinding? = null
    private val binding get() = _binding!!
    private val viewmodel : UpdateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateNumberBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}