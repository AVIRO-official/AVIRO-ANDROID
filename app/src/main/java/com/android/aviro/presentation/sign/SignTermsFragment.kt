package com.android.aviro.presentation.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aviro.R
import com.android.aviro.databinding.FragmentSignTermsBinding


class SignTermsFragment: Fragment() {

    private val sharedViewModel: SignViewModel by activityViewModels()

    private var _binding: FragmentSignTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignTermsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = sharedViewModel
        binding.lifecycleOwner = this@SignTermsFragment

        binding.nextBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SignCompleteFragment())
                .addToBackStack("SignCompleteFragment") // 백 스택에 추가
                .commit()
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}