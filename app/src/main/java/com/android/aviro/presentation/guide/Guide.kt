package com.android.aviro.presentation.guide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.databinding.ActivityGuideBinding


class Guide : AppCompatActivity() {

    private lateinit var binding: ActivityGuideBinding
    //lateinit var ViewModel: GuideViewModel

    val frag1 = GuideSearchFragment()
    val frag2 = GuideRegisterFragment()
    val frag3 = GuideMenuFragment()
    val frag4 = GuideReviewFragment()
    val fragList = arrayOf(frag1, frag2, frag3, frag4)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ViewModel = GuideViewModel()

        binding.viewPager.adapter = adapter

        // ViewPager에 페이지 변경 리스너 설정
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // 페이지 슬라이딩 중
            }

            override fun onPageSelected(position: Int) {
                // 페이지 선택 시
            }

            override fun onPageScrollStateChanged(state: Int) {
                // 페이지 스크롤 상태 변경 시
            }
        })
    }


        // 뷰페이저 어댑터
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {
                fragList[position]
                return fragList[position]
            }
        }

    }
