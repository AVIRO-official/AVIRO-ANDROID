package com.aviro.android.presentation.aviro_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aviro.android.databinding.TimepickerMenuItemBinding



import androidx.recyclerview.widget.RecyclerView

class TimeAdapter(private val timeList: List<String>, val setTime: (String) -> Unit) : //private val listener: (String) -> Unit
    RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(val binding: TimepickerMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String) {
            binding.time.text = time

            binding.time.setOnClickListener {
                setTime(time)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(TimepickerMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = timeList[position]
        holder.bind(time)
    }

    override fun getItemCount(): Int {
        return timeList.size
    }


}