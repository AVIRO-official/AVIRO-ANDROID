package com.android.aviro.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentBottomsheetReviewBinding

class BottomSheetReview : Fragment() {

    private var _binding: FragmentBottomsheetReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentBottomsheetReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //binding.viewmodel = viewmodel
        //binding.lifecycleOwner = this

        return root
    }
}