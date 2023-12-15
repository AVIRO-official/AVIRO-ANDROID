package com.android.aviro.presentation.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentUpdateTimetableBinding

class UpdateTimetableFragment : Fragment() {

    private var _binding: FragmentUpdateTimetableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateTimetableBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

}