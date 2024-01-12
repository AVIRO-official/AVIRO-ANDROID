package com.android.aviro.presentation.home.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.android.aviro.R
import com.android.aviro.databinding.AddMenuLayoutBinding
import com.android.aviro.databinding.FragmentRegisterBinding
import com.android.aviro.presentation.home.ui.mypage.MypageViewModel


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: RegisterViewModel by hiltNavGraphViewModels(R.id.navigation_register)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //viewmodel = RegisterViewModel()
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        binding.aadMenuBtn.setOnClickListener {

            // 데이터 바인딩 인스턴스 생성
            val binding_addMenu: AddMenuLayoutBinding = AddMenuLayoutBinding.inflate(inflater, container, false)

            // 바인딩 클래스에 뷰모델 설정
            binding_addMenu.viewmodel = this.viewmodel
            binding_addMenu.lifecycleOwner = this

            // 레이아웃을 LinearLayout에 추가
            binding.menuList.addView(binding_addMenu.root)

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}