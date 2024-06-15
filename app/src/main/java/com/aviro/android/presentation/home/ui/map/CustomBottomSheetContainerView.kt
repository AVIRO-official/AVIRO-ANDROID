package com.aviro.android.presentation.home.ui.map

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CustomBottomSheetContainerView : FrameLayout {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var viewmodel: MapViewModel

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    fun setViewModel(viewmodel: MapViewModel) {
        this.viewmodel = viewmodel
    }

    fun setBottomSheetBehavior(behavior: BottomSheetBehavior<*>) {
        this.bottomSheetBehavior = behavior // 바텀시트 조작해줘야 하므로 바텀시트 BottomSheetBehavior를 이 커스텀 레아아웃 클래스에 셋팅해놔야 함
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // 터치 이벤트를 소비하여 ViewPager2로 전달하지 않음
        // 외부 영역을 클릭한 경우 바텀시트를 숨김
        if(viewmodel._bottomSheetState.value != 3) {
            // 터치 막기, 펼쳐짐
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            viewmodel._bottomSheetState.value = 3
            return true
        }

        /*when (ev!!.getAction()) {
          MotionEvent.ACTION_DOWN -> {
           }
           MotionEvent.ACTION_MOVE -> {
           }

            //MotionEvent.ACTION_UP -> {

            }*/

        return false

        }

    }
