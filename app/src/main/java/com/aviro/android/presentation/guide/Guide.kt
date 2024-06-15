package com.aviro.android.presentation.guide

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.aviro.android.databinding.ActivityGuideBinding


class Guide : AppCompatActivity() {

    private lateinit var binding: ActivityGuideBinding
    lateinit var viewmodel: GuideViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = GuidePagerAdapter(this)

        viewmodel = GuideViewModel(adapter)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


    }


}
