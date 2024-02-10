package com.android.aviro.presentation.home.ui.mypage

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.android.aviro.R
import com.android.aviro.databinding.FragmentChangeNicknameBinding
import com.android.aviro.databinding.FragmentMypageBinding

class NicknameChangeFragment : Fragment() {

    private var _binding: FragmentChangeNicknameBinding? = null
    private val binding get() = _binding!!

    // 이전 프래그먼트의 뷰모델을 가져와야 함 -> 아니면 그냥 새로은거 써도 될듯,,,
    val viewmodel: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChangeNicknameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        binding.backBtn.setOnClickListener {
            finishNicknameChangeFragment()
        }

        viewmodel.toastLiveData.observe(this, androidx.lifecycle.Observer {
            if(it == "200") {
                viewmodel.getMyInfo()
                finishNicknameChangeFragment()
                viewmodel._toastLiveData.value = null
            }
        })

        return root
    }

    fun finishNicknameChangeFragment() {
        val fragmentManager = parentFragmentManager.beginTransaction()
        fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
        fragmentManager.remove(this@NicknameChangeFragment).commit()


    }
}