package com.android.aviro.presentation.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.databinding.ActivityUpdateBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Update : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateBinding

    val frag1 = UpdateLocFragment()
    val frag2 = UpdateNumberFragment()
    val frag3 = UpdateTimetableFragment()
    val frag4 = UpdateHomepageFragment()
    val fragList = arrayOf(frag1, frag2, frag3, frag4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager(binding.viewPager, binding.tabLayout)

    }
    private fun setupViewPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {
                fragList[position]
                return fragList[position]
            }
        }
        // Add more fragments as needed

        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "위치"
                1 -> tab.text = "전화번호"
                2 -> tab.text = "영업시간"
                3 -> tab.text = "홈페이지"
            }
        }.attach()


    }


}