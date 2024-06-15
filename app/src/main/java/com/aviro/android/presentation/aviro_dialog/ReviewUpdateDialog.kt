package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.R
import com.aviro.android.databinding.ReviewUpdateDialogBinding

class ReviewUpdateDialog(context : Context, val onClickUpdate : () -> (Unit), val onClickDelete : () -> (Unit)) : Dialog(context)  {

    private lateinit var binding: ReviewUpdateDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReviewUpdateDialogBinding.inflate(LayoutInflater.from(context))
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
            onClickUpdate()
            dismiss()
        }
        binding.item2.setOnClickListener {
            onClickDelete()
            dismiss()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }


}