package com.aviro.android.presentation.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.aviro.android.R
import com.aviro.android.databinding.FragmentSignOptionBinding

class SignOptionFragment : Fragment() {

    private val sharedViewModel: SignViewModel by activityViewModels()

    private var _binding: FragmentSignOptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignOptionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.nextBtn.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_left_enter,
                R.anim.slide_right_exit,
            )
            fragmentManager
                .replace(R.id.fragment_container_view, SignTermsFragment())
                .addToBackStack(null) // 백 스택에 추가
                .commit()
        }

        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


