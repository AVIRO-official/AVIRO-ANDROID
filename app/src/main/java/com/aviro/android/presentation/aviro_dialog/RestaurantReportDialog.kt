package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.R
import com.aviro.android.databinding.RestaurantReportDialogBinding
import com.aviro.android.presentation.home.ui.map.MapViewModel

class RestaurantReportDialog(context : Context, val viewmodel : MapViewModel) : Dialog(context) {

    private lateinit var binding: RestaurantReportDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RestaurantReportDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 311
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_15)

        // 취소 가능 유무
        setCancelable(true)
        initListener()
    }

    fun initListener() {
        binding.item1.setOnClickListener {
            viewmodel.setReportCode(1)
            dismiss()
        }
        binding.item2.setOnClickListener {
            viewmodel.setReportCode(2)
            dismiss()
        }
        binding.item3.setOnClickListener {
            viewmodel.setReportCode(3)
            dismiss()
        }
        binding.cancel.setOnClickListener {
            dismiss()

        }
    }

}