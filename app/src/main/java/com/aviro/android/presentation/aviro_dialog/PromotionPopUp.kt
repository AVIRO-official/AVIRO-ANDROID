package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.amplitude.core.Amplitude
import com.aviro.android.common.AmplitudeUtils
import com.aviro.android.databinding.PromotionPopupBinding
import com.aviro.android.presentation.home.HomeViewModel
import com.aviro.android.presentation.home.ui.map.PromotionAdapter

class PromotionPopUp(context : Context, val imgList : List<String>, val viewmodel : HomeViewModel) : Dialog(context) {

    private lateinit var binding: PromotionPopupBinding
    val prefs = context.getSharedPreferences("promotion_24hours", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PromotionPopupBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 311
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        setCanceledOnTouchOutside(false)
        setCancelable(true)

        val adapter = PromotionAdapter()
        adapter.setImgData(imgList)
        adapter.setClickFuntion(listOf {
            gotoChallenge()
            dismiss()
        })
        binding.promotionListView.adapter = adapter

        binding.hoursCancelBtn.setOnClickListener {
            prefs.edit().putLong("isShow", System.currentTimeMillis()).apply()
            dismiss()

            AmplitudeUtils.didTappedNoMoreShowWelcome()
        }

        binding.cancelBtn.setOnClickListener {
            prefs.edit().putLong("isShow", -1).apply()
            dismiss()

            AmplitudeUtils.didTappedCloseWelcome()
        }
    }

    fun gotoChallenge() {
        viewmodel._currentNavigation.value = HomeViewModel.WhereToGo.MYPAGE
        viewmodel._isNavigation.value = true

        AmplitudeUtils.challengePresent()
        AmplitudeUtils.didTappedCheckWelcome()
    }



}