package com.aviro.android.presentation.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aviro.android.R
import com.aviro.android.databinding.FragmentBottomsheetReviewBinding
import com.aviro.android.domain.entity.member.MemberLevelUp
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.aviro_dialog.LevelUpPopUp
import com.aviro.android.presentation.aviro_dialog.RecommendPopUp
import com.aviro.android.presentation.aviro_dialog.ReviewReportBottomSheetDialog
import com.aviro.android.presentation.entity.RestaurantInfoForReviewEntity
import com.aviro.android.presentation.home.HomeViewModel
import com.aviro.android.presentation.home.ui.map.MapViewModel

class BottomSheetReview(val setReviewAmount : (Int) -> Unit) : Fragment() {

    private lateinit var homeViewmodel: HomeViewModel
    private lateinit var mapViewmodel: MapViewModel
    private lateinit var viewmodel: BottomSheetViewModel
    private var _binding: FragmentBottomsheetReviewBinding? = null
    private val binding get() = _binding!!
    lateinit var reviewAdapter :  ReviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentBottomsheetReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this

        viewmodel.getNickname()
        setAdapter()
        initObserver()
        initLisenter()

        return root
    }

    fun setViewModel(bottomSheetViewModel: BottomSheetViewModel, mapViewModel: MapViewModel, homeViewmodel: HomeViewModel) {
        this.viewmodel = bottomSheetViewModel
        this.mapViewmodel = mapViewModel
        this.homeViewmodel = homeViewmodel
    }

    fun setAdapter() {

        reviewAdapter = ReviewAdapter(viewmodel, //viewmodel.userNickname!!
            {item ->
                viewmodel._selectedReviewForReport.value = item

                val bottomSheetDialog = ReviewReportBottomSheetDialog() { code, content ->
                    viewmodel.reportReview(code, content)
                }
                // 후기 신고하기
                bottomSheetDialog.show(parentFragmentManager, "reportDialog")
            },
            // 후기 삭제하기
            {item ->
                deleteReview(item)},
            // 후기 수정하기
            {item ->
                updateReview(item)}
        )
        binding.reviewListView.adapter = reviewAdapter
    }


    fun initObserver() {
        viewmodel.reviewList.observe(viewLifecycleOwner) {

            it?.let {
                setReviewAmount(it.commentArray.size)
                if (it.commentArray.size == 0) {
                    binding.reviewAmountTextView.text = "0개"
                    binding.noReviewImage.visibility = View.VISIBLE
                    binding.reviewListView.removeAllViews()
                    (binding.reviewListView.adapter as ReviewAdapter).reviewList = null

                } else {
                    binding.reviewAmountTextView.text = it.commentArray.size.toString() + '개'
                    binding.noReviewImage.visibility = View.GONE
                    binding.reviewListView.removeAllViews()
                    (binding.reviewListView.adapter as ReviewAdapter).reviewList =
                        it.commentArray.toMutableList()
                    //(binding.reviewListView.adapter as ReviewAdapter).notifyDataSetChanged()
                }
            }
        }

        viewmodel.selectedReviewForUpdate.observe(viewLifecycleOwner) {
            it?.let {
                val intent = Intent(requireContext(), ReviewActivity::class.java)
                val restaurantInfo = RestaurantInfoForReviewEntity(viewmodel.restaurantSummary.value!!.placeId, viewmodel.restaurantSummary.value!!.title,
                    viewmodel.restaurantSummary.value!!.category,viewmodel.restaurantSummary.value!!.address, viewmodel.selectedMarker.value!!.veganTypeColor)

                intent.putExtra("restaurant", restaurantInfo)
                intent.putExtra("review", it)

                startActivityForResult(intent, getString(R.string.REVIEW_RESULT_OK).toInt())
            }
        }

        viewmodel.toastLiveData.observe(viewLifecycleOwner) {
            val toast = Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
            toast.show()
            viewmodel.getRestaurantReview(viewmodel._selectedMarker.value!!.placeId)
        }

        viewmodel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let { errorMsg ->
                AviroDialogUtils.createOneDialog(
                    requireContext(),
                    "Error",
                    "${it}",
                    "확인"
                ).show()
            }
        }
    }

    fun initLisenter() {
        binding.writingReviewBtn.setOnClickListener {
            val intent = Intent(requireContext(), ReviewActivity::class.java)
            val restaurantInfo = RestaurantInfoForReviewEntity(viewmodel.restaurantSummary.value!!.placeId, viewmodel.restaurantSummary.value!!.title,
                viewmodel.restaurantSummary.value!!.category,viewmodel.restaurantSummary.value!!.address, viewmodel.selectedMarker.value!!.veganTypeColor)

            intent.putExtra("restaurant", restaurantInfo)
            startActivityForResult(intent, getString(R.string.REVIEW_RESULT_OK).toInt())

        }
    }


    fun deleteReview(review : Review) {
        AviroDialogUtils.createTwoDialog(requireContext(), "후기 삭제하기","정말로 삭제하시겠어요?\n삭제하면 다시 복구할 수 없어요.",
            "아니요", "예", {viewmodel.deleteReview(review.commentId)}).show()

    }

    fun updateReview(review : Review) {
        val intent = Intent(requireContext(), ReviewActivity::class.java)
        val restaurantInfo = RestaurantInfoForReviewEntity(viewmodel.restaurantSummary.value!!.placeId, viewmodel.restaurantSummary.value!!.title,
            viewmodel.restaurantSummary.value!!.category,viewmodel.restaurantSummary.value!!.address, viewmodel.selectedMarker.value!!.veganTypeColor)

        intent.putExtra("restaurant", restaurantInfo)
        intent.putExtra("review", review)

        startActivityForResult(intent, getString(R.string.REVIEW_RESULT_OK).toInt())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // 리뷰 작성, 수정 후 돌아옴
            getString(R.string.REVIEW_RESULT_OK).toInt() -> {
                if(data != null) {

                    viewmodel.getRestaurantReview(viewmodel.selectedMarker.value!!.placeId)

                    val level = data!!.getParcelableExtra<MemberLevelUp>("level_up")
                    level?.let {
                        LevelUpPopUp(requireContext(), it, homeViewmodel).show()
                    }

                    val recommend = data!!.getBooleanExtra("recommend", false)

                    if(recommend) {
                        // 가게 추천 팝업 (후기 신규 등록일때만)
                        RecommendPopUp(requireContext(), viewmodel).show()
                    }
                }


            }
        }
    }
}