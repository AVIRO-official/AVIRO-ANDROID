package com.android.aviro.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.BuildConfig
import com.android.aviro.R
import com.android.aviro.databinding.ActivityHomeBinding
import com.android.aviro.presentation.guide.Guide
import com.android.aviro.presentation.home.ui.map.Map
import com.android.aviro.presentation.home.ui.mypage.MypageFragment
import com.android.aviro.presentation.home.ui.register.OnSwipeTouchListener
import com.android.aviro.presentation.home.ui.register.RegisterFragment
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : FragmentActivity() {

    private lateinit var binding: ActivityHomeBinding

    val frag1 = Map()
    val frag2 = RegisterFragment()
    val frag3 = MypageFragment()

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



       /* val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_mypage, R.id.navigation_map, R.id.navigation_register
            )
        )*/

        //navView.setupWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)


        //맨 처음 보여줄 플래그먼트 데려오기
        //fragmentManager = supportFragmentManager
        //val fm0 = supportFragmentManager.beginTransaction()
        //fm0.replace(R.id.nav_host_fragment_activity_home, Map()).commitAllowingStateLoss()
        //mapFragment = Map()
        //supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_home, mapFragment!!).commit()


        //바텀 네비게이션에 연동
        /*
        navView.setOnItemSelectedListener {
            //val fm = supportFragmentManager.beginTransaction()
            when(it.itemId){
                R.id.navigation_map ->{
                    if(mapFragment == null){
                        mapFragment = Map()

                    }
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_home, Map())
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                    //supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_home, mapFragment!!).commit()
                    /*if(mapFragment != null) supportFragmentManager.beginTransaction().show(mapFragment!!).commit()
                    if(registerFragment != null) supportFragmentManager.beginTransaction().hide(registerFragment!!).commit()
                    if(mypageFragment != null) supportFragmentManager.beginTransaction().hide(mypageFragment!!).commit()*/

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_register ->{

                    if(registerFragment == null){
                        registerFragment = RegisterFragment()

                    }
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_home, RegisterFragment())
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                   /* if(mapFragment != null) supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if(mypageFragment != null) supportFragmentManager.beginTransaction().hide(mypageFragment!!).commit()
                    if(registerFragment != null) supportFragmentManager.beginTransaction().show(registerFragment!!).commit() */
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_mypage ->{
                    if(mypageFragment == null){
                        mypageFragment = MypageFragment()

                    }
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_home,mypageFragment!!).commitAllowingStateLoss()
//                    if(mapFragment != null) supportFragmentManager.beginTransaction().hide(mapFragment!!).commit()
//                    if(registerFragment != null) supportFragmentManager.beginTransaction().hide(registerFragment!!).commit()
//                    if(mypageFragment != null)supportFragmentManager.beginTransaction().show(mypageFragment!!).commit()

                    return@setOnItemSelectedListener true
                }
                else ->{
                    return@setOnItemSelectedListener true
                }
            }

    }

         */


/*
    private fun setupViewPager(viewPager: ViewPager2, nav: BottomNavigationView) {
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {
                //fragList[position]
                return fragList[position]
            }
        }
        viewPager.isUserInputEnabled = false
        /*
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // 특정 페이지에서의 스와이프 제한 로직
                if (position == 1) {
                    // position이 1일 때, 0 방향으로의 스와이프만 허용
                    viewPager.isUserInputEnabled = positionOffset > 0
                } else {
                    // 그 외의 페이지에서는 스와이프 허용
                    viewPager.isUserInputEnabled = false
                }
            }
        })

         */

        nav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_map -> binding.homePager.currentItem = 0

                R.id.navigation_register -> binding.homePager.currentItem = 1

                R.id.navigation_mypage -> binding.homePager.currentItem = 2


            }
            return@setOnItemSelectedListener true
        }



        viewPager.adapter = adapter

    }
   */