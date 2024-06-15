package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.R
import com.aviro.android.databinding.TimetableDialogBinding
import com.aviro.android.domain.entity.restaurant.RestaurantTimetable

class TimetableDialog (context : Context, val titmetable : RestaurantTimetable) : Dialog(context) {

    private lateinit var binding: TimetableDialogBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimetableDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)


        val widthInDp = 343
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_15)


        binding.timetable = titmetable

        // 다이얼로그 바깥쪽 클릭시 종료
        setCanceledOnTouchOutside(false)
        // 취소 가능 유무
        setCancelable(true)

        initListener()
    }



    fun initListener() {
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}