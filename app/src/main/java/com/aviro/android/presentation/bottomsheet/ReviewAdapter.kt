package com.aviro.android.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.R
import com.aviro.android.databinding.ReviewItemBinding
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.presentation.aviro_dialog.ReviewReportDialog
import com.aviro.android.presentation.aviro_dialog.ReviewUpdateDialog

class ReviewAdapter(val viewmodel : BottomSheetViewModel,
                    val onclickReport :(Review)->(Unit),
                    val onClickDelete :(Review)->(Unit),
                    val onClickUpdate :(Review)->(Unit)) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){
                        // val onClickBlock :(String)->(Unit)
                        // val onClickUnBlock :(String)->(Unit)

    //val userNickname : String,
    // viewmodel : BottomSheetViewModel,
    var reviewList: MutableList<Review>? = null
    //var userNickname : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) //ReviewViewHolder.from(parent, userNickname)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        if (reviewList != null) {
            holder.bind(reviewList!![position])
        }
    }
    override fun getItemCount() = reviewList?.size ?: 0



    inner class ReviewViewHolder(
        val binding: ReviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.review = item
            // 내가 쓴 리뷰일 경우
            if(item.nickname == viewmodel.userNickname) {
                binding.reviewContainer.background = ContextCompat.getDrawable(binding.root.context, R.drawable.base_roundsquare_bgblue_10)
                binding.content.setTextColor(ContextCompat.getColor(binding.root.context, R.color.Cobalt))
            } else {
                // 내가 차단한 사용자의 후기인 경우 로직
                /*
                if(item.nickname == ) {
                    binding.reviewContainer.background = ContextCompat.getDrawable(binding.root.context, R.drawable.base_roundsquare_gray6)
                    binding.content.setTextColor(ContextCompat.getColor(binding.root.context, R.color.Gray4))
                    binding.content.text = "차단한 사용자의 후기입니다."
                }
                 */

                binding.reviewContainer.background = ContextCompat.getDrawable(binding.root.context, R.drawable.base_roundsquare_gray6)
                binding.content.setTextColor(ContextCompat.getColor(binding.root.context, R.color.Gray0))
            }
            binding.executePendingBindings()

            binding.updateBtn.setOnClickListener {
                if(item.nickname == viewmodel.userNickname) {
                    ReviewUpdateDialog(binding.root.context, {onClickUpdate(item)},
                        {onClickDelete(item)}).show()

                    /*AviroDialogUtils.createTwoChoiceDialog(binding.root.context, "더보기", "수정하기", "삭제하기",
                        //viewmodel.moveForUpdateReview(item.commentId, item.content)
                        {onClickUpdate(item)},
                        {onClickDelete(item)}).show() */
                } else {
                    ReviewReportDialog(binding.root.context, {onclickReport(item)}).show()
                    // 내가 차단한 사용자의 후기인 경우 로직
                    /*
                    if(item.nickname == ) {
                           ReviewReportDialog(binding.root.context, {onclickReport(item), {onClickUnBlock()}}).show()
                    } else {
                     ReviewReportDialog(binding.root.context, {onclickReport(item), {onClickBlock()}}).show()

                     */
                }

            }
        }

    }

}