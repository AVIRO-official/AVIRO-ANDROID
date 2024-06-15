package com.aviro.android.presentation.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2


class GuideViewModel(val adapter : GuidePagerAdapter) : ViewModel() {

    val _dot = MutableLiveData<List<Boolean>>()
    val dot : LiveData<List<Boolean>>
        get() = _dot

    init {
        _dot.value = listOf(true, false, false, false)

        adapter.addFragment(GuideSearchFragment())
        adapter.addFragment(GuideRegisterFragment())
        adapter.addFragment(GuideMenuFragment())
        adapter.addFragment(GuideReviewFragment())

    }

    val pageCallBack = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            // 페이지 슬라이딩 중
        }
        override fun onPageSelected(position: Int) {
            //super.onPageSelected(position)
            when(position) {
                0 -> _dot.value = listOf(true, false, false, false)
                1 ->  _dot.value = listOf(false, true, false, false)
                2 ->  _dot.value = listOf(false, false, true, false)
                3 ->  _dot.value = listOf(false, false, false, true)
            }
        }
        override fun onPageScrollStateChanged(state: Int) {
            // 페이지 스크롤 상태 변경 시
        }
    }


}