package com.android.aviro.presentation.sign

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySignBinding
import com.android.aviro.presentation.BaseActivity
import com.android.aviro.presentation.home.Home
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class Sign : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    //val viewModel by viewModels<SignViewModel>()
    private val viewmodel: SignViewModel by viewModels() //뷰모델 의존성 주입

    lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel = ViewModelProvider(this, ViewModelFactory(CreateSocialAccountUseCase(), CreateUserUseCase())).get(SignViewModel::class.java)

        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, SignNicknameFragment())
            .addToBackStack("SignNicknameFragment")
            .commit()


        viewmodel.isSignUp.observe(this, androidx.lifecycle.Observer{
            if(it) { //
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SignNicknameFragment())
                    .addToBackStack("SignNicknameFragment")
                    .commit()
            } else {
                startActivity(Intent(this, Home::class.java))
            }

        })

    }


    // 로그인 정보가 담긴 인텐트를 받는 메소드
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewmodel._isSign.value = false

        intent.data?.let { uri ->
            parseUri(uri)
        }
    }

    private fun parseUri(url: Uri){
        val idTokenParam = url.getQueryParameter("id_token")
        val codeParam = url.getQueryParameter("code")

        if (idTokenParam != null && codeParam != null){
            Log.d("idTokenParam", "${idTokenParam}")
            Log.d("codeParam", "${codeParam}")

            // 로그인 처리할 동안 로그인 버튼 비활성화 및 프로그레스바 생성
            viewmodel.checkSignUp(idTokenParam, codeParam)

        }

    }

    override fun onBackPressed() {
        //super.onBackPressed();
    }


}