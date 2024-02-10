package com.android.aviro.presentation.home.ui.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

import com.google.android.material.bottomsheet.BottomSheetBehavior


class CustomFragmentContainerView : FrameLayout {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var preState = ""

    fun setBottomSheetBehavior(behavior: BottomSheetBehavior<*>) {
        this.bottomSheetBehavior = behavior
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // 터치 이벤트를 소비하여 ViewPager2로 전달하지 않음
        // 외부 영역을 클릭한 경우 바텀시트를 숨김

        when (ev!!.getAction()) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {
                Log.d("map","onTouchEvent")
                preState = "move"
            }
            MotionEvent.ACTION_UP -> {
                if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN){
                    return false
                } else {
                    if(preState == "move") {
                        return false
                    } else  {
                        // 다른 마커 클릭 -> 데이터만 변경
                        // 외부 화면 클릭 -> 사라짐
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        return true
                    }
                }

            }
        }
        return false

        /*
        if (ev?.action == MotionEvent.ACTION_UP) {
            Log.d("map","MotionEvent.ACTION_UP")
            val rect = IntArray(2)
            getLocationOnScreen(rect)
            val left = rect[0]
            val top = rect[1]
            val right = left + width
            val bottom = top + height
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()
            if (x < left || x > right || y < top || y > bottom) {
                Log.d("map","onTouchEvent")
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return true
            }
        }
        return false

         */
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // 외부 영역을 클릭한 경우 바텀시트를 숨김
        return true
    }
}