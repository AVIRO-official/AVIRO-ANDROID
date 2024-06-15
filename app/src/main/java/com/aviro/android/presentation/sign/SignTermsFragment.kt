package com.aviro.android.presentation.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.R
import com.aviro.android.databinding.FragmentSignTermsBinding
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils


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
                AviroDialogUtils.createOneDialog(requireContext(), "Error",it,"확인").show()

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