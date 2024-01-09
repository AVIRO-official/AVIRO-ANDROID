package com.android.aviro.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.android.aviro.R
import com.android.aviro.presentation.guide.Guide
import com.android.aviro.presentation.sign.Sign

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        goToHomeOrGuideOrSignWithDelay()
        /*
        Handler().postDelayed(Runnable {

            val prefs = getPreferences(Context.MODE_PRIVATE)
            val firstRun = prefs.getBoolean("firstRun", true)

            if (firstRun) {
                // 처음 실행될 때의 작업 수행
                startActivity(Intent(this, Guide::class.java))
                // "처음 실행 여부"를 false로 변경
                prefs.edit().putBoolean("firstRun", false).apply()

            } else {
                startActivity(Intent(this, Sign::class.java))
            }

            // 현재 액티비티 닫기
            finish()
        }, 3000)
*/
    }



    fun goToHomeOrGuideOrSignWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if(isFirstVisitor) {
                onboardingNavigator.intent(this)
            } else if(viewmodel.isSignedUp()) {
                homeNavigator.intent(this)
            }
            else {
                signupNavigator.intent(this)
            }
            startActivity(intent)
            finish()
        }, DELAYED_MILLIS)
    }


}