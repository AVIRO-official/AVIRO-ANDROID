package com.android.aviro.presentation.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.aviro.databinding.FragmentGuideReviewBinding
import com.android.aviro.presentation.sign.Sign


class GuideReviewFragment : Fragment() {

    private var _binding: FragmentGuideReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : GuideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGuideReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = GuideViewModel("review")


        _binding!!.startBtn.setOnClickListener {
            val next_activity = Intent(requireContext(), Sign::class.java)
            startActivity(next_activity)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}