package com.android.aviro.presentation.sign

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySignBinding
import com.android.aviro.domain.usecase.user.CreateSocialAccountUseCase
import com.android.aviro.domain.usecase.user.CreateUserUseCase
import com.android.aviro.presentation.BaseActivity
import com.android.aviro.presentation.ViewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel


class Sign : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    //lateinit var binding: ActivitySignBinding
    //lateinit var sharedViewModel: SignViewModel
    //val viewModel by viewModels<SignViewModel>()
     lateinit var viewModel: SignViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(CreateSocialAccountUseCase(), CreateUserUseCase()))
            .get(SignViewModel::class.java)

        //binding = ActivitySignBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // 뷰모델 설정
        //sharedViewModel = SignViewModel()

        //sharedViewModel.lifecycleOwner = this@Sign

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()


        fragmentTransaction
            .replace(R.id.fragment_container_view, SignNicknameFragment())
            .addToBackStack("SignNicknameFragment") // 백 스택에 추가
            .commit()



    }


}