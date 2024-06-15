package com.aviro.android.presentation.aviro_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.R
import com.aviro.android.databinding.TimepickerMenuBinding

class TimePickerDialog(context: Context, val timeList: List<String>, val setTime: (String) -> Unit)  : Dialog(context) {

    private lateinit var binding: TimepickerMenuBinding
    lateinit var timeAdpater : TimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TimepickerMenuBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val widthInDp = 188
        val density = context.resources.displayMetrics.density
        val widthInPixels = (widthInDp * density).toInt()
        window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(R.drawable.base_roundsquare_white_15)

        setAdapter()

    }

    fun setAdapter() {
        timeAdpater = TimeAdapter(timeList) { selectedTime ->
            setTime(selectedTime)
            dismiss()
        }
        binding.timepickerList.adapter = timeAdpater
    }


}