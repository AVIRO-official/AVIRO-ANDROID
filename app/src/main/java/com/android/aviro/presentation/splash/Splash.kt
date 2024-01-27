package com.android.aviro.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySplashBinding
import com.android.aviro.presentation.guide.Guide
import com.android.aviro.presentation.home.Home
import com.android.aviro.presentation.sign.Sign
import com.android.aviro.presentation.sign.SignViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewmodel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewmodel.isSignIn.observe(this, Observer { isSignIn ->
            if (isSignIn == true) {
                startActivity(Intent(this@Splash, Home::class.java))
                finish()
            } else {
                startActivity(Intent(this@Splash, Sign::class.java))
                finish()

            }
        })

        goToHomeOrGuideOrSignWithDelay()


    }

    fun goToHomeOrGuideOrSignWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
        }, 3000)
        // val intent =
        if (viewmodel.isFirstStartApp()) { // 앱 최초 실행인지 확인 필
            //Intent(this, Guide::class.java)
            startActivity(Intent(this, Guide::class.java))
            finish()
        } else {
            runBlocking {
                val job = async(Dispatchers.IO) {
                    viewmodel.isSignIn()
                }
                job.await() // job이 완료될 때까지 대기
            }
        }
    }
}

        /*
        Handler(Looper.getMainLooper()).postDelayed({

        }, 3000)

         */