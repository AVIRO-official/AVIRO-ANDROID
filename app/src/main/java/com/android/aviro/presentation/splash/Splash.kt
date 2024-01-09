package com.android.aviro.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.android.aviro.R
import com.android.aviro.presentation.guide.Guide
import com.android.aviro.presentation.home.Home
import com.android.aviro.presentation.sign.Sign
import com.android.aviro.presentation.sign.SignViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash : AppCompatActivity() {

    private val viewmodel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        goToHomeOrGuideOrSignWithDelay()

    }


    fun goToHomeOrGuideOrSignWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if(viewmodel.isFirstStartApp()) { // 앱 최초 실행인지 확인 필
                Intent(this, Guide::class.java)
            } else if(viewmodel.isSignIn()) {             // 자동로그인
                Intent(this, Home::class.java)
            } else { //로그인 필요
                Intent(this, Sign::class.java)
            }

            startActivity(intent)
            finish()
        }, 3000)
    }




}