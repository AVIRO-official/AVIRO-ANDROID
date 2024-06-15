package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.aviro.android.R
import com.aviro.android.databinding.SearchLocationDialogBinding

class SortingLocationDialog (context : Context, val sortType : String, val onClickMap : () -> (Unit), val onClickCurrLoc : () -> (Unit)) : Dialog(context)  {

    private lateinit var binding: SearchLocationDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchLocationDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 311
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_30)

        // 현재 정렬
        when(sortType) {
            "내위치중심" -> binding.myLocRadioButton.background =  ContextCompat.getDrawable(context, R.drawable.checkbox_blue)
            "지도중심" -> binding.mapLocRadioButton.background =  ContextCompat.getDrawable(context, R.drawable.checkbox_blue)
        }


        setCanceledOnTouchOutside(false)
        setCancelable(true)

        initListener()
    }

    fun initListener() {
        binding.item1.setOnClickListener {
            onClickMap() // 맵중심
            dismiss()

        }
        binding.item2.setOnClickListener {
            onClickCurrLoc() // 내위치 중심
            dismiss()

        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }


}