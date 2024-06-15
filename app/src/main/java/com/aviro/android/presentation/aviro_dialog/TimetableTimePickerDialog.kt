package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.aviro.android.R

import com.aviro.android.databinding.TimetableTimepickerDialogBinding
import com.aviro.android.presentation.entity.OperatingTimeEntity
import com.aviro.android.presentation.update.UpdateViewModel


class TimetableTimePickerDialog(val day : String,  val viewmodel : UpdateViewModel, val open : String, val breakTime : String) : DialogFragment() { //updateViewmodel : UpdateViewModel

    private var _binding: TimetableTimepickerDialogBinding? = null
    private val binding get() = _binding!!

    private var resultCallback: ((String, String) -> Unit)? = null

    lateinit var operatingEntity : OperatingTimeEntity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)

        //setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        val widthInDp = 188
        val density = requireContext().resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        dialog.window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_15)

        return dialog


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TimetableTimepickerDialogBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        initUI()
        initListener()
        initObserver()



        return binding.root

    }

    fun initUI() {

        if(open == "휴무") {
            operatingEntity = OperatingTimeEntity(day, true, false, false,  "","","","")
            binding.workingBtn.isChecked = false
            binding.holidayBtn.isChecked = true
            viewmodel.setSelectedOperatingData(operatingEntity)
            //_operating.value =  operatingEntity
        } else {
            val openTime = if(open == "" ||open == null || open == "정보 없음") "" else open.split('-')[0]
            val closeTime = if(open == "" ||open == null|| open == "정보 없음") "" else open.split('-')[1]
            val breakStartTime = if(breakTime == "" || breakTime == null || breakTime == "정보 없음") "" else  breakTime.split('-')[0]
            val breakEndTime = if(breakTime == ""  || breakTime == null || breakTime == "정보 없음") "" else breakTime.split('-')[1]
            val isBreak = !(breakTime == ""  || breakTime == null || breakTime == "정보 없음")

            binding.workingBtn.isChecked = true
            binding.holidayBtn.isChecked = false

            operatingEntity = OperatingTimeEntity(day, false, isBreak, false, openTime, closeTime, breakStartTime, breakEndTime)
            viewmodel.setSelectedOperatingData(operatingEntity)
            //_operating.value =  operatingEntity

            binding.timeUpdateBtn.isEnabled = false
            binding.timeUpdateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.base_roundsquare_gray6_30)
            binding.timeUpdateTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray2))

        }
    }

    fun setResultCallback(callback : (String, String) -> Unit) {
        this.resultCallback = callback
    }

    private fun setResultAndDismiss(open: String, breakTime: String) {
        resultCallback?.invoke(open, breakTime)
        dismiss()
    }


    fun initObserver() {

        viewmodel.SelectedOperatingData.observe(viewLifecycleOwner) {
            Log.d("SelectedOperatingData","${it}")
            // 시작 시간 - 마감 시간 (휴식도 동일)
            if((it.openTime != "" && it.closeTime == "") || (it.closeTime != "" && it.openTime == "") ||
                (it.breakStartTime != "" && it.breakEndTime == "") || (it.breakEndTime != "" && it.breakStartTime == "")) {
                binding.timeUpdateBtn.isEnabled = false
                binding.timeUpdateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.base_roundsquare_gray6_30)
                binding.timeUpdateTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray2))
            } else {
                binding.timeUpdateBtn.isEnabled = true
                binding.timeUpdateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.base_roundsquare_cobalt_30)
                binding.timeUpdateTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray7))
            }
        }
    }

    fun initListener() {
        binding.openTimePicker.setOnClickListener {
            val timeList = generateTimeList()
            val dialog = TimePickerDialog(requireContext(), timeList) { selectedTime ->
                operatingEntity.openTime = selectedTime
                viewmodel.setSelectedOperatingData(operatingEntity)
                //_operating.value = operatingEntity
                Log.d("selectedTime","${selectedTime}")
                //Log.d("selectedTime","${_operating.value!!}")

            }
            dialog.show()
        }
        binding.closeTimePicker.setOnClickListener {
            //showTimePicker(viewmodel._closeTime)
            val timeList = generateTimeList()
            val dialog = TimePickerDialog(requireContext(), timeList) { selectedTime ->
                operatingEntity.closeTime = selectedTime
                viewmodel.setSelectedOperatingData(operatingEntity)
                //_operating.value = operatingEntity


            }
            dialog.show()
        }
        binding.breakStartTimePicker.setOnClickListener {
            //showTimePicker(viewmodel._breakStartTime)
            val timeList = generateTimeList()
            val dialog = TimePickerDialog(requireContext(), timeList) { selectedTime ->
                operatingEntity.breakStartTime = selectedTime
                operatingEntity.isBreak = true
                viewmodel.setSelectedOperatingData(operatingEntity)
                //_operating.value = operatingEntity
            }
            dialog.show()
        }
        binding.breakEndTimePicker.setOnClickListener {
            //showTimePicker(viewmodel._breakEndTime)
            val timeList = generateTimeList()
            val dialog = TimePickerDialog(requireContext(), timeList) { selectedTime ->
                operatingEntity.breakEndTime = selectedTime
                operatingEntity.isBreak = true
                //_operating.value = operatingEntity
                viewmodel.setSelectedOperatingData(operatingEntity)
            }
            dialog.show()
        }

        binding.timeUpdateBtn.setOnClickListener {
            // 뷰의 내용들을 추출
            if(viewmodel.SelectedOperatingData.value!!.isHoliday) {
                setResultAndDismiss("휴무", "")
            } else {
                // 영업시간 파싱
                var open : String
                if(viewmodel.SelectedOperatingData.value!!.openTime != "" && viewmodel.SelectedOperatingData.value!!.closeTime != "") {
                     open = "${viewmodel.SelectedOperatingData.value!!.openTime}-${viewmodel.SelectedOperatingData.value!!.closeTime}"
                } else {
                    open = ""
                }

                // 휴식시간 있으면 파신
                var breakTime : String
                if(viewmodel.SelectedOperatingData.value!!.breakStartTime != "" && viewmodel.SelectedOperatingData.value!!.breakEndTime != "") {
                    breakTime = "${viewmodel.SelectedOperatingData.value!!.breakStartTime}-${viewmodel.SelectedOperatingData.value!!.breakEndTime}"
                } else {
                    breakTime = ""
                }

                setResultAndDismiss(open, breakTime)
            }
             }

        binding.cancelBtn.setOnClickListener {
            Log.d("TimetableTimePickerDialog","cancelBtn")
            setResultAndDismiss(open, breakTime)
            //dismiss()
        }

        // 휴식없음 클릭
        binding.noBreakBtn.setOnClickListener {
            if(operatingEntity.isBreak) {
                operatingEntity.breakStartTime = ""
                operatingEntity.breakEndTime = ""
                operatingEntity.isBreak = false
            } else {
                operatingEntity.isBreak = true
            }
            //_operating.value = operatingEntity
            viewmodel.setSelectedOperatingData(operatingEntity)

        }

        // 24시간 클릭
        binding.allHoursBtn.setOnClickListener {
            if(operatingEntity.isAllHours) {
                operatingEntity.isAllHours = false
            } else {
                operatingEntity.openTime = "00:00"
                operatingEntity.closeTime = "24:00"
                operatingEntity.isAllHours = true
            }

            //_operating.value = operatingEntity
            viewmodel.setSelectedOperatingData(operatingEntity)
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.workingBtn -> {
                    operatingEntity.isHoliday = false
                    operatingEntity.isAllHours = false
                    operatingEntity.isBreak = true
                    //_operating.value = operatingEntity
                    viewmodel.setSelectedOperatingData(operatingEntity)
                }
                R.id.holidayBtn -> { 
                    operatingEntity.isHoliday = true
                    operatingEntity.isAllHours = false
                    operatingEntity.isBreak = false
                    operatingEntity.openTime = ""
                    operatingEntity.closeTime = ""
                    operatingEntity.breakStartTime = ""
                    operatingEntity.breakEndTime = ""
                    //_operating.value = operatingEntity
                    viewmodel.setSelectedOperatingData(operatingEntity)
                }

            }
        }


    }




    private fun generateTimeList(): List<String> {
        val timeList = mutableListOf<String>()
        for (hour in 0 until 24) {
            for (minute in 0 until 60 step 10) {
                val time = String.format("%02d:%02d", hour, minute)
                timeList.add(time)
            }
        }
        return timeList
    }

    override fun onDestroy() {
        super.onDestroy()
        //operating.removeObservers(viewLifecycleOwner)
        _binding = null
    }





}