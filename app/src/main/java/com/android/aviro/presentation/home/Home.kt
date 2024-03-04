package com.android.aviro.presentation.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.BuildConfig
import com.android.aviro.R
import com.android.aviro.databinding.ActivityHomeBinding
import com.android.aviro.presentation.home.ui.map.Map
import com.android.aviro.presentation.home.ui.mypage.ChallengeFragment
import com.android.aviro.presentation.home.ui.register.RegisterFragment
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : FragmentActivity() {

    private lateinit var binding: ActivityHomeBinding

    val frag1 = Map()
    val frag2 = RegisterFragment()
    val frag3 = ChallengeFragment()

    val fragList = arrayOf(frag1, frag2, frag3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 싱글톤
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIENT_ID)


        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {

                return fragList[position]
            }
        }

        binding.homePager.registerOnPageChangeCallback(PageChangeCallback())
        binding.homePager.isUserInputEnabled = false


        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_map -> {
                    //binding.homePager.currentItem = 0
                    binding.homePager.setCurrentItem(0, false)
                    binding.navView.isVisible = true
                    binding.homePager.isUserInputEnabled = false

                }

                R.id.navigation_register -> {
                    //binding.homePager.currentItem = 1
                    binding.homePager.setCurrentItem(1, false)
                    binding.homePager.isUserInputEnabled = false
                    binding.navView.isVisible = false
                    //val registerIntent = Intent(this, RegisterFragment::class.java)
                    //startActivity(registerIntent)

                }

                R.id.navigation_mypage -> {
                    //binding.homePager.currentItem = 2
                    binding.homePager.setCurrentItem(2, false)
                    binding.navView.isVisible = true
                    binding.homePager.isUserInputEnabled = false
                }
            }
            return@setOnItemSelectedListener true
        }

        binding.homePager.adapter = adapter


        // 마이페이지 정보 가져오기


    }

    /*
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("TouchEventTest", "called dispatchTouchEvent() in CustomLayout")
        frag1.dispatchBottomSheet(ev!!)
        return super.dispatchTouchEvent(ev)
    }

     */



    override fun onResume() {
        Log.d("HomeAvtivity", "onResume")
        super.onResume()
    }

    private inner class PageChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.navView.selectedItemId = when (position) {
                0 -> R.id.navigation_map
                1 -> R.id.navigation_register
                2 -> R.id.navigation_mypage
                else -> error("no such position: $position")
            }
        }
    }

    // GPS 권한 설정 콜백
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 요청 코드가 일치하고 결과 코드가 성공인 경우
        if (requestCode == getString(R.string.SEARCH_RESULT_OK).toInt() && resultCode == Activity.RESULT_OK) {
            //val resultData = data?.getStringExtra("result_key")
            // 결과 데이터를 프래그먼트로 전달
            val fragment = supportFragmentManager.findFragmentById(R.id.home_pager)
            if (fragment is Map) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }

        }


}


abstract class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    private val gestureDetector: GestureDetector
    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val distanceX = e2.x - e1!!.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) > Math.abs(distanceY)
                && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return false
        }
    }
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}

