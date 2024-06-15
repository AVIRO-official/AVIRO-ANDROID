package com.aviro.android.presentation.sign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.aviro.android.R
import com.aviro.android.databinding.ActivitySignBinding
import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.presentation.BaseActivity
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.home.Home
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class Sign : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    //val viewModel by viewModels<SignViewModel>()
    private val viewmodel: SignViewModel by viewModels() //뷰모델 의존성 주입

    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

   
        val action = intent.getStringExtra("Action")

        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, SignSocialFragment())
            .addToBackStack("SignSocialFragment")
            .commit()

        action?.let {
            when(it) {
                R.string.LOGOUT.toString() -> {
                    Toast.makeText(this, "로그아웃이 완료되었습니다.", Toast.LENGTH_LONG).show()
                }

                R.string.WITHDRAW.toString() -> {
                    AviroDialogUtils.createOneDialog(this, "회원탈퇴가 완료되었어요.","함깨한 시간이 아쉽지만,\n" +
                            "언제든지 돌아오는 문을 열어둘게요.\n어비로의 비건 여정은 계속될 거예요!","확인").show()
                }
            }
        }

        // 회원가입 해야 하는지 확인
        viewmodel.isSignUp.observe(this, androidx.lifecycle.Observer{
            if(it) {
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
        viewmodel._isSignIn.value = false

        intent.data?.let { uri ->
            Log.d("appleData","${uri}")
            parseUri(uri)
        }
    }

    private fun parseUri(url: Uri){
        val idTokenParam = url.getQueryParameter("id_token")
        val codeParam = url.getQueryParameter("code")
        val email = url.getQueryParameter("email")
        val name : String?  = url.getQueryParameter("name")

        if (idTokenParam != null && codeParam != null && email != null){
            // 로그인 처리할 동안 로그인 버튼 비활성화 및 프로그레스바 생성
            viewmodel.createTokens(APPLE, idTokenParam, codeParam, email, name)

        }

    }





}