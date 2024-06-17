package com.aviro.android.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.aviro.android.R
import com.aviro.android.databinding.ActivitySplashBinding
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.presentation.guide.Guide
import com.aviro.android.presentation.home.Home
import com.aviro.android.presentation.sign.Sign
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewmodel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewmodel.isSignIn.observe(this, Observer { isSignIn ->
            if (isSignIn == true) {
                // 어떤 로그인인지 확인
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
        }, 5000)

        if (isFirstStartApp()) { // 앱 최초 실행인지 확인 필
            startActivity(Intent(this, Guide::class.java))
            finish()
        } else {
            runBlocking {
                val job = async(Dispatchers.IO) {
                    /*
                    // 구글 로그인 있는지 확인
                    val account = GoogleSignIn.getLastSignedInAccount(this@Splash)
                    if(account == null) {
                        // 다른 자동 로그인 확인
                        viewmodel.isSignIn()
                    } else {
                        // userId 있는지 확인
                        // 구글 자동로그인
                        viewmodel.isSignIn()
                    }
                     */
                    viewmodel.isSignIn()

                }
                job.await() // job이 완료될 때까지 대기
            }
        }
    }



    fun isFirstStartApp() : Boolean {
        val prefs = this.getSharedPreferences("first_visitor", MODE_PRIVATE)
        val firstRun = prefs.getBoolean("firstRun", true) // 처음엔 default 값 출력

        if (firstRun) {
            // 처음 실행될 때의 작업 수행
            // "처음 실행 여부"를 false로 변경
            prefs.edit().putBoolean("firstRun", false).apply()
        }

        return firstRun
    }



}
