package com.android.aviro.presentation.guide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.databinding.ActivityGuideBinding


class Guide : AppCompatActivity() {

    private lateinit var binding: ActivityGuideBinding
    lateinit var viewmodel: GuideViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = GuidePagerAdapter(this)

        viewmodel = GuideViewModel(adapter)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


    }


}
