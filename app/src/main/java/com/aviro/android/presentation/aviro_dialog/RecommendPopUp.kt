package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.aviro.android.R
import com.aviro.android.databinding.RecommendPopupBinding
import com.aviro.android.presentation.bottomsheet.BottomSheetViewModel


class RecommendPopUp(context : Context, val viewmodel : BottomSheetViewModel) : Dialog(context) {

    private lateinit var binding: RecommendPopupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecommendPopupBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 311
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_15)

        // 다이얼로그 바깥쪽 클릭시 종료되도록 함 (Cancel the dialog when you touch outside)
        setCanceledOnTouchOutside(false)
        // 취소 가능 유무
        setCancelable(true)


        initListener()
    }



    fun initListener() {
        binding.yesBtn.setOnClickListener {
            viewmodel.postRecommend(true)
            dismiss()

            val thanksToast = Toast.makeText(
                context, "소중한 정보 감사합니다!", Toast.LENGTH_SHORT
            )
            thanksToast.show()
        }
        binding.noBtn.setOnClickListener {
            viewmodel.postRecommend(false)
            dismiss()
        }
    }



}