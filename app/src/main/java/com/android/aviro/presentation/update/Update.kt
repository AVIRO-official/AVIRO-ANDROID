package com.android.aviro.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.databinding.ActivityUpdateBinding

class Update : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding

    val frag2 = UpdateLocFragment()
    val frag3 = UpdateNumberFragment()
    val frag4 = UpdateTimetableFragment()
    val frag1 = UpdateHomepageFragment()
    val fragList = arrayOf(frag1, frag2, frag3, frag4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

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