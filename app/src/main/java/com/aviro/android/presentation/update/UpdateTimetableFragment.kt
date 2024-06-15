package com.aviro.android.presentation.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.databinding.FragmentUpdateTimetableBinding
import com.aviro.android.presentation.aviro_dialog.TimetableTimePickerDialog


class UpdateTimetableFragment : Fragment() {

    private var _binding: FragmentUpdateTimetableBinding? = null
    private val binding get() = _binding!!
    private val viewmodel : UpdateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateTimetableBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        val root : View = binding.root

        initListener()
        initObserver()

        return root

    }


    fun initListener() {

        binding.monday.setOnClickListener {

            val dialog = TimetableTimePickerDialog("월요일", viewmodel,
                viewmodel.afterTimetableData.value!!.mon,  viewmodel.afterTimetableData.value!!.monBreak)

            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                // value값 자체가 변해야 ui에 반영
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(mon = open, monBreak = breakTime)
            }
            dialog.show(childFragmentManager, "TimetableTimePickerDialog")

        }
        binding.tuesday.setOnClickListener {
            //showTimePicker("tue", viewmodel.restaurantTimetable.value!!.tue)
            val dialog = TimetableTimePickerDialog("화요일", viewmodel,
                viewmodel.afterTimetableData.value!!.tue,
                viewmodel.afterTimetableData.value!!.tueBreak )

            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(tue = open, tueBreak = breakTime)
            }

            dialog.show(childFragmentManager, "TimetableTimePickerDialog")
        }

        binding.wednesday.setOnClickListener {
            //showTimePicker("wed", viewmodel.restaurantTimetable.value!!.wed)
            val dialog = TimetableTimePickerDialog("수요일", viewmodel,
                viewmodel.afterTimetableData.value!!.wed,  viewmodel.afterTimetableData.value!!.wedBreak)

            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(wed = open, wedBreak = breakTime)
            }

            dialog.show(childFragmentManager, "TimetableTimePickerDialog")
        }
        binding.thursday.setOnClickListener {
            //showTimePicker("wed", viewmodel.restaurantTimetable.value!!.thu)
            val dialog = TimetableTimePickerDialog("목요일", viewmodel,
                viewmodel.afterTimetableData.value!!.thu,  viewmodel.afterTimetableData.value!!.thuBreak)

            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(thu = open, thuBreak = breakTime)
            }

            dialog.show(childFragmentManager, "TimetableTimePickerDialog")
        }
        binding.friday.setOnClickListener {
            //showTimePicker("wed", viewmodel.restaurantTimetable.value!!.fri)
            val dialog = TimetableTimePickerDialog("금요일", viewmodel,
                viewmodel.afterTimetableData.value!!.fri,  viewmodel.afterTimetableData.value!!.friBreak)

            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(fri = open, friBreak = breakTime)
            }

            dialog.show(childFragmentManager, "TimetableTimePickerDialog")
        }
        binding.saturday.setOnClickListener {
            //showTimePicker("wed", viewmodel.restaurantTimetable.value!!.sat)
            val dialog = TimetableTimePickerDialog("토요일", viewmodel,
                viewmodel.afterTimetableData.value!!.sat,  viewmodel.afterTimetableData.value!!.satBreak)

            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(sat = open, satBreak = breakTime)
            }
            dialog.show(childFragmentManager, "TimetableTimePickerDialog")
        }
        binding.sunday.setOnClickListener {
            //showTimePicker("wed", viewmodel.restaurantTimetable.value!!.sun)
            val dialog = TimetableTimePickerDialog("일요일", viewmodel,
                viewmodel.afterTimetableData.value!!.sun,  viewmodel.afterTimetableData.value!!.sunBreak)
            dialog.isCancelable = false
            dialog.setResultCallback { open, breakTime ->
                viewmodel._afterTimetableData.value = viewmodel._afterTimetableData.value?.copy(sun = open, sunBreak = breakTime)
            }
            dialog.show(childFragmentManager, "TimetableTimePickerDialog")
        }

    }

    fun initObserver() {
        viewmodel.afterTimetableData.observe(viewLifecycleOwner) {
            Log.d("checkChangedTimetable", "${it}")
            viewmodel.checkChangedTimetable()

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}