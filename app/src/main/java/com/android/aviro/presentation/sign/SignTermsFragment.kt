package com.android.aviro.presentation.sign

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aviro.R
import com.android.aviro.databinding.FragmentSignTermsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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

        /*
        binding.nextBtn.setOnClickListener {
            if(sharedViewModel.isNext.value == true) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SignCompleteFragment())
                    .addToBackStack("SignCompleteFragment") // 백 스택에 추가
                    .commit()
            }
        }

         */
        sharedViewModel.isComplete.observe(this, androidx.lifecycle.Observer{
            if(it == true) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SignCompleteFragment())
                    .addToBackStack("SignCompleteFragment") // 백 스택에 추가
                    .commit()
            }
        })

        sharedViewModel.errorLiveData.observe(this, androidx.lifecycle.Observer{
            if(it != null) {
                MaterialAlertDialogBuilder(ContextThemeWrapper(requireContext(), R.style.AVIRO_Dialog))
                    .setTitle("Error")
                    .setMessage(it)
                    .setPositiveButton("확인") { dialog, which ->
                        // 권한 설정 거부
                        // 위치 추적 없이 구동
                        dialog.cancel()
                    }
                    .show()
            }

        })

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