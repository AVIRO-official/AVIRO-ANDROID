package com.android.aviro.presentation.guide

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aviro.R

class GuidePagerAdapter(fragmentActivity : Guide) : FragmentStateAdapter(fragmentActivity)
{
    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    // 어댑터가 특정 위치(position)의 Fragment 반환
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    // 어댑터가 가지고 있는 Fragment 개수 반환
    override fun getItemCount(): Int {
        return fragmentList.size
    }

}