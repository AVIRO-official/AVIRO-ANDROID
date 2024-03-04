package com.android.aviro.presentation.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentBottomsheetHomeBinding
import com.android.aviro.databinding.FragmentMapBinding

class BottomSheetHome : Fragment() {

    private var _binding: FragmentBottomsheetHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentBottomsheetHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //binding.viewmodel = viewmodel
        //binding.lifecycleOwner = this

        return root
    }
}