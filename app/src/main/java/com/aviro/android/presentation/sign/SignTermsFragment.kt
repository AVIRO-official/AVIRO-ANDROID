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

        sharedViewModel.isComplete.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if(it == true) {
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.setCustomAnimations(
                    R.anim.slide_left_enter, R.anim.slide_right_exit,
                    R.anim.slide_right_enter, R.anim.slide_left_exit
                )
                fragmentManager
                    .replace(R.id.fragment_container_view, SignCompleteFragment())
                    .addToBackStack(null) // 백 스택에 추가
                    .commit()
            }
        })

        sharedViewModel.errorLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if(it != null) {
                AviroDialogUtils.createOneDialog(requireContext(), "Error",it,"확인").show()

            }

        })

        binding.backBtn.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_right_enter,
                R.anim.slide_left_exit
            )
            fragmentManager.remove(this@SignTermsFragment).commit()
            //requireActivity().onBackPressed()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}