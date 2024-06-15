package com.aviro.android.presentation.aviro_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.aviro.android.R
import com.aviro.android.databinding.ChallengeGuideBottomsheetDialogBinding
import com.aviro.android.presentation.home.ui.mypage.MypageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChallengeGuideDialog(val viewmodel : MypageViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: ChallengeGuideBottomsheetDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ChallengeGuideBottomsheetDialogBinding.inflate(inflater, container, false)
        val cancelBtn =  binding.root.findViewById<Button>(R.id.gotItBtn)
        binding.viewmodel = viewmodel

        cancelBtn.setOnClickListener {
            dismiss()
        }


  return binding.root
}
}