package com.aviro.android.presentation.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.R
import com.aviro.android.databinding.FragmentSignNicknameBinding
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils

class SignNicknameFragment() : Fragment() {

    private val sharedViewModel: SignViewModel by activityViewModels()

    private var _binding: FragmentSignNicknameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignNicknameBinding.inflate(inflater, container, false)
        val view = binding.root

        //sharedViewModel = SignViewModel()
        binding.viewmodel = sharedViewModel
        binding.lifecycleOwner = this@SignNicknameFragment

        binding.nextBtn.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(
                R.anim.slide_right_enter,
                R.anim.slide_left_exit,
                R.anim.slide_left_enter,
                R.anim.slide_right_exit,
            )
            fragmentManager
                .replace(R.id.fragment_container_view, SignOptionFragment())
                .addToBackStack(null) // 백 스택에 추가
                .commit()
        }

        binding.backBtn.setOnClickListener {
            sharedViewModel.cancelSignUp()
            //val fragmentManager = parentFragmentManager.beginTransaction()
            //fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            parentFragmentManager.popBackStack()
        //fragmentManager.remove(this@SignNicknameFragment).commit()
            //this.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        // 프래그먼트가 활성화될 때 뒤로가기 버튼의 동작을 막음
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}