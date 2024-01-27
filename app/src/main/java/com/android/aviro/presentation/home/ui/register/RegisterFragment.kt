package com.android.aviro.presentation.home.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.R
import com.android.aviro.databinding.*
import com.android.aviro.presentation.home.Home
import com.android.aviro.presentation.home.ui.mypage.MypageViewModel


class RegisterFragment : Fragment()  { //

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    //private val viewmodel: RegisterViewModel by hiltNavGraphViewModels(R.id.navigation_register)
    private val viewmodel: RegisterViewModel by viewModels()//(R.id.navigation_register)

    //private lateinit var mGestureDetector : GestureDetector

    //SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        binding.scroll.setOnTouchListener(object: OnSwipeTouchListener(context = requireContext()) {
            override fun onSwipeLeft() {
                Log.d("Swipe","onLeft")
            }
            override fun onSwipeRight() {
                Log.d("Swipe","onRight")
                // 뷰페이저 슬라이드 가능
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.home_pager)
                var current = viewPager.currentItem
                if (current == 1){
                    //binding.scrollLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_left))
                    //overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
                    viewPager.setCurrentItem(0, true)
                }
            }
        })

        binding.aadMenuBtn.setOnClickListener {

            // 데이터 바인딩 인스턴스 생성
            val binding_addMenu: AddMenuLayoutBinding =
                AddMenuLayoutBinding.inflate(layoutInflater)

            // 바인딩 클래스에 뷰모델 설정
            binding_addMenu.viewmodel = this.viewmodel
            binding_addMenu.lifecycleOwner = this

            // 레이아웃을 LinearLayout에 추가
            binding.menuList.addView(binding_addMenu.root)

        }

        binding.backBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.home_pager)
            viewPager.setCurrentItem(0, true)
        }



        return root


            }

    fun back() {
        //this.onBackPressed()
    }


}

abstract class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    companion object {
        // 반응성 조절
        private const val SWIPE_DISTANCE_THRESHOLD = 200
        private const val SWIPE_VELOCITY_THRESHOLD = 200
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
                if (distanceX > 0) {
                    onSwipeRight()
                } else onSwipeLeft()
                return true
            }
            return false
        }
    }
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}


