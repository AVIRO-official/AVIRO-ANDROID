package com.android.aviro.presentation.home.ui.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.R
import com.android.aviro.databinding.ActivityHomeBinding
import com.android.aviro.databinding.ActivityMypageBinding
import com.android.aviro.databinding.FragmentMypageBinding
import com.android.aviro.databinding.FragmentRegisterBinding
import com.android.aviro.presentation.home.ui.register.OnSwipeTouchListener
import com.android.aviro.presentation.home.ui.register.RegisterViewModel

class MypageActivity : Fragment() {


    private var _binding: ActivityMypageBinding? = null
    private val binding get() = _binding!!

    // 이전 프래그먼트의 뷰모델을 가져와야 함 -> 아니면 그냥 새로은거 써도 될듯,,,
    val viewmodel: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        binding.backBtn.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            fragmentManager.remove(this@MypageActivity).commit()

        }

        binding.MainLinearLayout.setOnTouchListener(object: OnSwipeTouchListener(context = requireContext()) {
            override fun onSwipeLeft() {
                Log.d("Swipe","onLeft")
            }
            override fun onSwipeRight() {
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
                fragmentManager.remove(this@MypageActivity).commit()
            }
        })

        return root


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


