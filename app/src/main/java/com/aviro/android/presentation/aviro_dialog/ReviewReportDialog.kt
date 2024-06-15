package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.R
import com.aviro.android.databinding.ReviewReportDialogBinding

class ReviewReportDialog(context : Context, val onClickReport : () -> (Unit)) : Dialog(context)  {

    private lateinit var binding: ReviewReportDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReviewReportDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 311
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_30)

        setCanceledOnTouchOutside(false)
        setCancelable(true)

        initListener()
    }

    fun initListener() {
        binding.item1.setOnClickListener {
            // 후기 신고 이유 고를 수 있음
            onClickReport()
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }
    }


}