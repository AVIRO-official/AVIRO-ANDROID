package com.android.aviro.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySignBinding


class Sign : AppCompatActivity() {

    lateinit var binding: ActivitySignBinding
    lateinit var sharedViewModel: SignViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뷰모델 설정
        sharedViewModel = SignViewModel()
        //binding.viewmodel = viewModel
        //sharedViewModel.lifecycleOwner = this@Sign

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction
            .replace(R.id.fragment_container_view, SignNicknameFragment())
            .addToBackStack("SignNicknameFragment") // 백 스택에 추가
            .commit()

    }



}