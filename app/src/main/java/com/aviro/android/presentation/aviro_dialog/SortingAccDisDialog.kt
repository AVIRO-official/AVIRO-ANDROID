package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.aviro.android.R
import com.aviro.android.databinding.SearchSortDialogBinding

class SortingAccDisDialog (context : Context, val sortType : String, val onClickAcc : () -> (Unit), val onClickDistance : () -> (Unit)) : Dialog(context)  {

    private lateinit var binding: SearchSortDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchSortDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 311
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_30)


        when(sortType) {
            "정확도순" -> binding.accSortRadioButton.background =  ContextCompat.getDrawable(context, R.drawable.checkbox_blue)
            "거리순" -> binding.distSortRadioButton.background =  ContextCompat.getDrawable(context, R.drawable.checkbox_blue)
        }

        setCanceledOnTouchOutside(false)
        setCancelable(true)

        initListener()
    }

    fun initListener() {
        binding.item1.setOnClickListener {
            onClickAcc() // 정확도순
            dismiss()

        }
        binding.item2.setOnClickListener {
            onClickDistance() // 거리순
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }
    }


}