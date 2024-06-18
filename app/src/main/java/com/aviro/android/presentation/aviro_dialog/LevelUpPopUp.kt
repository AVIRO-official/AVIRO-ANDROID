package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.R
import com.aviro.android.databinding.LevelupPopupBinding
import com.aviro.android.domain.entity.member.MemberLevelUp
import com.aviro.android.presentation.home.HomeViewModel

class LevelUpPopUp(context : Context, val levelup : MemberLevelUp, val viewmodel : HomeViewModel) : Dialog(context) {

    private lateinit var binding: LevelupPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LevelupPopupBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 390
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.textview2.setText("레벨" + levelup.userLevel.toString() + "달성했어요!")



        // 다이얼로그 바깥쪽 클릭시 종료되도록 함 (Cancel the dialog when you touch outside)
        setCanceledOnTouchOutside(false)

        // 취소 가능 유무
        setCancelable(true)

        initListener()
    }



    fun initListener() {
        binding.completeBtn.setOnClickListener {
            // 마이페이지로 이동 // 뷰페이저 이동
            viewmodel._currentNavigation.value = HomeViewModel.WhereToGo.MYPAGE
            viewmodel._isNavigation.value = true
            dismiss()
        }
        binding.nextBtn.setOnClickListener {
            dismiss()
        }
    }



}