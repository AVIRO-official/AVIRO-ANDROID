package com.aviro.android.presentation.aviro_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.aviro.android.R

import com.aviro.android.databinding.ReviewReportListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewReportBottomSheetDialog(val onSelectCode : (Int, String?) -> (Unit)) : BottomSheetDialogFragment() {//val viewmodel : BottomSheetViewModel

    private lateinit var binding: ReviewReportListDialogBinding

    private val radioBtnList = mutableListOf(R.id.rg_btn1, R.id.rg_btn2, R.id.rg_btn3, R.id.rg_btn4, R.id.rg_btn5, R.id.rg_btn6, R.id.rg_btn7)

    private var reportCode : Int = 0
    private var reportContent : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ReviewReportListDialogBinding.inflate(inflater, container, false)
        //viewmodel._reviewReportNumber.value = 0

        initListener()

        return binding.root
    }


    fun initObserver() {

    }

    fun changeRadioButton(idx : Int) {
        // 클릭 되지 않은 라디오 버튼 체크 해제
        for(i in 0 until  radioBtnList.size) {
            if(i != idx ) {
                val radioBtn = binding.root.findViewById<RadioButton>(radioBtnList[i])
                radioBtn.isChecked = false
            } else {
                reportCode = idx

            }
        }

        if(idx == 6) {
            binding.reportContentContainer.visibility = View.VISIBLE
        } else {
            binding.reportContentContainer.visibility = View.GONE
        }
    }


    fun initListener() {

        binding.rgBtn1.setOnClickListener {
            changeRadioButton(0)
        }
        binding.rgBtn2.setOnClickListener {
            changeRadioButton(1)
        }
        binding.rgBtn3.setOnClickListener {
            changeRadioButton(2)
        }
        binding.rgBtn4.setOnClickListener {
            changeRadioButton(3)
        }
        binding.rgBtn5.setOnClickListener {
            changeRadioButton(4)
        }
        binding.rgBtn6.setOnClickListener {
            changeRadioButton(5)
        }
        binding.rgBtn7.setOnClickListener {
            changeRadioButton(6)
        }

        binding.reportContent.addTextChangedListener {
            reportContent = it.toString()
            if(reportContent == "") {
                binding.reportBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray3))
                binding.reportBtn.isEnabled = false
            } else {
                binding.reportBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.Cobalt))
                binding.reportBtn.isEnabled = true
            }
        }

        binding.reportBtn.setOnClickListener {
            if(reportCode == 6) {

                onSelectCode(reportCode, reportContent)
            } else {
                onSelectCode(reportCode, null)
            }

            dismiss()
        }

        binding.backBtn.setOnClickListener {
            dismiss()
        }
    }



}