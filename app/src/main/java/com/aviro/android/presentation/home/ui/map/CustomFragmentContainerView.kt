package com.aviro.android.presentation.home.ui.map

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.aviro.android.domain.entity.marker.MarkerOfMap

import com.google.android.material.bottomsheet.BottomSheetBehavior


class CustomFragmentContainerView : FrameLayout {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var viewmodel: MapViewModel

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val TOUCH_THRESHOLD = 200 //10
    private var startX = 0f
    private var startY = 0f

    private var preSelectedMarker : MarkerOfMap? = null

    fun setViewModel(viewmodel: MapViewModel) {
        this.viewmodel = viewmodel // 바텀시트 조작해줘야 하므로 바텀시트 BottomSheetBehavior를 이 커스텀 레아아웃 클래스에 셋팅해놔야 함
    }

    fun setBottomSheetBehavior(behavior: BottomSheetBehavior<*>) {
        this.bottomSheetBehavior = behavior // 바텀시트 조작해줘야 하므로 바텀시트 BottomSheetBehavior를 이 커스텀 레아아웃 클래스에 셋팅해놔야 함
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // 터치 이벤트를 소비하여 ViewPager2로 전달하지 않음
        // 외부 영역을 클릭한 경우 바텀시트를 숨김
        when (ev!!.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                // 화면에 손가락이 닿을 때 호출
                startX = ev!!.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                // 손가락이 화면에 닿은채로 움직일때 호출 (단순 터치를 할때도 호출 되기 때문에 이 이벤트로 뭔가를 판별하긴 어려움)
            }

            MotionEvent.ACTION_UP -> {
                // 화면에서 손가락이 떨어졌을 때 호출
                val endX = ev!!.getX()
                val endY = ev.getY()

                val distance = Math.sqrt(
                    ((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY)).toDouble()
                ).toFloat()

                // 손가락이 화면이 닿았을 때 위치와 떨어질때 위치를 비교해 일정 이상 차이나면 단순 터치가 아닌 드래그로 간주
                if (distance < TOUCH_THRESHOLD) {
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) { // 바텀시트가 up 되어 있다면
                        // 바텀시트 숨기기
                        viewmodel._isShowBottomSheetTab.value = false
                        performClick()

                    }
                }
        }
        /* 만약 맵 탐색하는 드래그 동작이거나 바텀시트가 up 되어 있지 않는 상태에서 마커를 클릭한 경우에는
         * false를 반환하여 동작이 네이버맵으로 흘러가도록 해야 함
         */
    }
        return false
    }


}