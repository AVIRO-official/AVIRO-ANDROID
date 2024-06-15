package com.aviro.android.presentation.home.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.databinding.MyReviewItemBinding
import com.aviro.android.databinding.MyReviewItemBinding.*
import com.aviro.android.domain.entity.member.MyComment
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.entity.BoxIcon

class MyReviewAdapter(val onClick: (MyComment)->(Unit), val onClickDelete: (MyComment)->(Unit)) : RecyclerView.Adapter<MyReviewAdapter.MyReviewViewHolder>() {


    var reviewList: MutableList<MyComment>? = null

    inner class MyReviewViewHolder (val binding: MyReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyComment) {
            binding.myReviewItem = item
            binding.iconType = BoxIcon(item.category, item.allVegan, item.someMenuVegan, item.ifRequestVegan)

            binding.item.setOnClickListener {
                onClick(item)
            }

            binding.reviewDeleteBtn.setOnClickListener {
                AviroDialogUtils.createTwoDialog(binding.root.context,
                    "정말로 선택한 후기를\n삭제하시겠어요?",
                    "삭제하면 다시 복구할 수 없고\n챌린지 포인트가 다시 회수돼요.",
                    "취소",
                    "삭제하기", {onClickDelete(item)}).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewViewHolder {
        return MyReviewViewHolder(MyReviewItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: MyReviewViewHolder, position: Int) {
        if (reviewList != null) {
            holder.bind(reviewList!![position])
        }
    }

    override fun getItemCount() = reviewList?.size ?: 0

     fun setData(reviewList: MutableList<MyComment>){
         this.reviewList= reviewList
         notifyDataSetChanged()
     }



      /*  companion object {
            fun from(parent: ViewGroup): MyReviewViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = inflate(layoutInflater, parent, false)

                return MyReviewViewHolder(binding)
            }
        }*/


}