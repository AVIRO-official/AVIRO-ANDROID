package com.aviro.android.presentation.home.ui.map

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.aviro.android.databinding.PromotionItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners


class PromotionAdapter : BaseAdapter() {

    // 이미지, 버튼색, 메서드
    private lateinit var popupImgList : List<String>
    private lateinit var funcList : List<() -> Unit>
    override fun getCount(): Int {
        return popupImgList.size
    }

    override fun getItem(p0: Int): String {
       return popupImgList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        /*
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false)
        }
         */
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val binding = PromotionItemBinding.inflate(layoutInflater, parent, false)


        Glide.with(parent.context)
            .load(popupImgList[position])
            .transform(CenterCrop(), GranularRoundedCorners(30f, 30f, 0f, 0f)) // 상단 모서리 둥글게
            .into(binding.popupImg)

        val shapeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(parent.context, com.aviro.android.R.color.Green)) // 매번 바뀔 수 있음
            cornerRadii = floatArrayOf(
                0f, 0f,
                0f, 0f,
                30f, 30f, // Bottom right radius in px
                30f, 30f // Bottom left radius in px
            )
        }

        binding.popupBtn.background = shapeDrawable


        binding.popupBtn.setOnClickListener {
            funcList[position].invoke()
        }

        //convertView.setOnClickListener(View.OnClickListener { onItemClicked(position) })


        return binding.root
    }

    fun setImgData(imageList : List<String>) {
        imageList.let {
            this.popupImgList = imageList
        }
    }

    fun setClickFuntion(funcList : List<() -> Unit>) {
        funcList.let {
            this.funcList = funcList
        }
    }

    fun setColor() {

    }



}