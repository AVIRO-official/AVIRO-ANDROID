package com.android.aviro.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.android.aviro.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            // 앱의 main activity로 넘어가기
            val next_activity = Intent(this@Splash, Guide::class.java)
            startActivity(next_activity)
            // 현재 액티비티 닫기
            finish()
        }, 3000)

    }
}