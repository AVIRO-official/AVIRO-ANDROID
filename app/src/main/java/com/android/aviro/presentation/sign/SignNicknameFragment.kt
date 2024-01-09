package com.android.aviro.presentation.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aviro.R
import com.android.aviro.databinding.FragmentSignNicknameBinding
import com.android.aviro.databinding.FragmentSignSocialBinding

class SignNicknameFragment : Fragment() {

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
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SignOptionFragment())
                .addToBackStack("SignOptionFragment") // 백 스택에 추가
                .commit()
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