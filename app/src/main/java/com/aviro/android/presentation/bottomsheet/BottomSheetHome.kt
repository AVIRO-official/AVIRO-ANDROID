package com.aviro.android.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.aviro.android.R
import com.aviro.android.databinding.FragmentBottomsheetHomeBinding
import com.aviro.android.domain.entity.member.MemberLevelUp
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.aviro_dialog.LevelUpPopUp
import com.aviro.android.presentation.aviro_dialog.RecommendPopUp
import com.aviro.android.presentation.aviro_dialog.RestaurantReportDialog
import com.aviro.android.presentation.aviro_dialog.ReviewReportBottomSheetDialog
import com.aviro.android.presentation.aviro_dialog.TimetableDialog
import com.aviro.android.presentation.entity.RestaurantInfoForReviewEntity
import com.aviro.android.presentation.home.HomeViewModel
import com.aviro.android.presentation.home.ui.map.MapViewModel
import com.aviro.android.presentation.update.Update
import com.aviro.android.presentation.update.UpdateMenu

class BottomSheetHome(val setReviewAmount : (Int) -> Unit) : Fragment() {

    private lateinit var homeViewmodel: HomeViewModel
    private lateinit var mapViewmodel: MapViewModel
    private lateinit var viewmodel: BottomSheetViewModel
    private var _binding: FragmentBottomsheetHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var menuAdapter :  MenuAdapter
    lateinit var reviewAdapter : ReviewAdapter

    enum class UpdateType {
        LOC, TIME, NUMBER, HOMEPAGE
    }


    // 사용자에게 화면이 그려질때 생성됨
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomsheetHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.getNickname()

        setAdapter()
        initObserver()
        initListener()

        return root
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("level_up")
        }
    }

    fun setViewModel(bottomSheetViewModel: BottomSheetViewModel, mapViewModel: MapViewModel,  homeViewmodel: HomeViewModel) {
        this.viewmodel = bottomSheetViewModel
        this.mapViewmodel = mapViewModel
        this.homeViewmodel = homeViewmodel
    }

    private fun setAdapter() {
        menuAdapter = MenuAdapter()
        Log.d("BottomSheetHome:ERROR", "${viewmodel.userNickname}")
        reviewAdapter = ReviewAdapter(viewmodel,  //viewmodel.userNickname!!
            {item ->
                viewmodel._selectedReviewForReport.value = item
                Log.d("_selectedReviewForReport", "${item}")

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

            /*
            // 사용자 차단
            {userId ->
                userBlock(userId)}
            // 사용자 차단 해제
            {userId ->
                userUnBlock(userId)}

             */
        )



        binding.menuListView.adapter = menuAdapter
        binding.reviewListView.adapter = reviewAdapter
    }

    private fun initListener() {

        // 메뉴 더보기
        binding.moreMenuBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.bottomsheetViewPager)
            viewPager.setCurrentItem(1, true)
        }

        binding.moreReviewBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.bottomsheetViewPager)
            viewPager.setCurrentItem(2, true)
        }


        binding.writingReviewBtn.setOnClickListener {
            // 후기 작성 화면으로 이동
            val intent = Intent(requireContext(), ReviewActivity::class.java)
            val restaurantInfo = RestaurantInfoForReviewEntity(viewmodel.restaurantSummary.value!!.placeId, viewmodel.restaurantSummary.value!!.title,
                viewmodel.restaurantSummary.value!!.category,viewmodel.restaurantSummary.value!!.address, viewmodel.selectedMarker.value!!.veganTypeColor)
            intent.putExtra("restaurant", restaurantInfo)
            startActivityForResult(intent, getString(R.string.REVIEW_RESULT_OK).toInt())
        }

        binding.infoUpdatebtn.setOnClickListener {
            // 업데이트 화면
            viewmodel.setRestaurantData(mapViewmodel.selectedMarker.value!!)
            val intent = Intent(requireContext(), Update::class.java)
            intent.putExtra("NaviType", UpdateType.LOC.toString())
            intent.putExtra("RestaurantInfo", viewmodel.restaurantDataForUpdate.value)
            intent.putExtra("RestaurantTimeTable", viewmodel.restaurantTimetable.value)
            startActivityForResult(intent, getString(R.string.UPDATE_RESULT_OK).toInt())
        }
        binding.menuUpdateBtn.setOnClickListener {
            // 메뉴 업데이트 화면
            viewmodel.setRestaurantData(mapViewmodel.selectedMarker.value!!)

            val intent = Intent(requireContext(), UpdateMenu::class.java)
            intent.putExtra("RestaurantInfo", viewmodel.restaurantDataForUpdate.value)
            startActivityForResult(intent, getString(R.string.UPDATE_MENU_RESULT_OK).toInt())
        }

        binding.reportBtn.setOnClickListener {
            RestaurantReportDialog(requireContext(), mapViewmodel).show()
        }
    }

    private fun initObserver() {

        viewmodel.menuList.observe(viewLifecycleOwner) {
            binding.menuAmountTextView.text = it.count.toString() + '개'
            binding.moreMenuBtn.visibility = if (it.count > 5) View.VISIBLE else View.GONE
            binding.menuUpdatedDate.text = "업데이트 " + it.updatedTime

            binding.menuListView.removeAllViews()
            (binding.menuListView.adapter as MenuAdapter).menuList = it.menuArray.take(5).toMutableList()
            //(binding.menuListView.adapter as MenuAdapter).notifyDataSetChanged()
        }

        viewmodel.reviewList.observe(viewLifecycleOwner) {
            it?.let {
                setReviewAmount(it.commentArray.size)
                if(it.commentArray.size == 0) {
                    binding.reviewAmountTextView.text = "0개"
                    binding.moreReviewBtn.visibility = View.GONE
                    binding.noReviewText.visibility = View.VISIBLE
                    binding.reviewListView.removeAllViews()
                    (binding.reviewListView.adapter as ReviewAdapter).reviewList = null
                } else {
                    binding.noReviewText.visibility = View.GONE
                    binding.reviewAmountTextView.text = it.commentArray.size.toString() + '개'
                    binding.moreReviewBtn.visibility = if (it.commentArray.size > 5) View.VISIBLE else View.GONE

                    binding.reviewListView.removeAllViews()
                    (binding.reviewListView.adapter as ReviewAdapter).reviewList = it.commentArray.take(5).toMutableList()
                }

            }

        }

        viewmodel.restaurantInfo.observe(viewLifecycleOwner) {
            binding.restaurantInfo = it

            // 영업 시간
            if(it.haveTime) {
                binding.tiemtableTextView.text = it.shopStatus + ' ' + it.shopHours
                binding.tiemtableTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray0))
                binding.moreTiemtable.setOnClickListener {
                    if(viewmodel.bottomSheetSate.value != 2){
                        // 영업시간 더보기
                        TimetableDialog(requireContext(), viewmodel.restaurantTimetable.value!!).show()
                    }
                }

            } else {
                binding.tiemtableTextView.text = "영업 시간 추가"
                binding.tiemtableTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Keyword_Blue))
                binding.tiemtableTextView.setOnClickListener {
                    if (viewmodel.bottomSheetSate.value != 2) {
                        viewmodel.setRestaurantData(mapViewmodel.selectedMarker.value!!)

                        val intent = Intent(requireContext(), Update::class.java)
                        intent.putExtra("NaviType", UpdateType.TIME.toString())
                        intent.putExtra("RestaurantInfo", viewmodel.restaurantDataForUpdate.value)
                        intent.putExtra("RestaurantTimeTable", viewmodel.restaurantTimetable.value)
                        startActivityForResult(intent, getString(R.string.UPDATE_RESULT_OK).toInt())
                    }
                }
                }



            // 가게 전화번호
            if(it.phone == "" || it.phone == null) {
                binding.phoneTextView.setText("전화번호 추가")

                if(viewmodel.bottomSheetSate.value == 2) {
                    binding.phoneTextView.setOnClickListener(null)
                } else {
                    binding.phoneTextView.setOnClickListener { view ->
                        // 업데이트 화면
                        val intent = Intent(requireContext(), Update::class.java)
                        intent.putExtra("NaviType",UpdateType.NUMBER.toString())
                        intent.putExtra("RestaurantInfo", viewmodel.restaurantDataForUpdate.value)
                        intent.putExtra("RestaurantTimeTable", viewmodel.restaurantTimetable.value)
                        startActivityForResult(intent, getString(R.string.UPDATE_RESULT_OK).toInt())
                    }
                }
            } else {
                binding.phoneTextView.setText(it.phone)
                if(viewmodel.bottomSheetSate.value == 2) {
                    binding.phoneTextView.setOnClickListener(null)
                } else {
                    binding.phoneTextView.setOnClickListener { view ->
                    // 전화 걸기
                        val intent = Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + it.phone.replace("-","")));
                        startActivity(intent)
                    }
                }
            }

            // 가게 홈페이지
            if(it.url == "" || it.url == null) {
                binding.homepageTextView.setText("홈페이지 링크 추가")

                if(viewmodel.bottomSheetSate.value == 2) {
                    binding.homepageTextView.setOnClickListener(null)
                } else {
                    binding.homepageTextView.setOnClickListener { view ->
                        // 업데이트 화면
                        viewmodel.setRestaurantData(mapViewmodel.selectedMarker.value!!)

                        val intent = Intent(requireContext(), Update::class.java)
                        intent.putExtra("NaviType",UpdateType.HOMEPAGE.toString())
                        intent.putExtra("RestaurantInfo", viewmodel.restaurantDataForUpdate.value)
                        intent.putExtra("RestaurantTimeTable", viewmodel.restaurantTimetable.value)
                        startActivityForResult(intent, getString(R.string.UPDATE_RESULT_OK).toInt())
                    }
                }

            } else {
                binding.homepageTextView.setText(it.url)
                if(viewmodel.bottomSheetSate.value == 2) {
                    binding.homepageTextView.setOnClickListener(null)
                } else {
                    binding.homepageTextView.setOnClickListener { view ->
                        // 홈페이지 이동
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                        startActivity(intent)
                    }
                }
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


    fun deleteReview(review : Review) {
        viewmodel.deleteReview(review.commentId)
    }

    fun updateReview(review : Review) {
        val intent = Intent(requireContext(), ReviewActivity::class.java)
        val restaurantInfo = RestaurantInfoForReviewEntity(viewmodel.restaurantSummary.value!!.placeId, viewmodel.restaurantSummary.value!!.title,
            viewmodel.restaurantSummary.value!!.category,viewmodel.restaurantSummary.value!!.address, viewmodel.selectedMarker.value!!.veganTypeColor)

        intent.putExtra("restaurant", restaurantInfo)
        intent.putExtra("review", review)

        startActivityForResult(intent, getString(R.string.REVIEW_RESULT_OK).toInt())
    }

    fun userBlock() {
        AviroDialogUtils.createTwoDialog(binding.root.context, "더보기", "이 사용자의 후기를 모두 차단하시겠어요?",
            "취소",
            "차단") {
             } // viewmodel.userBlock(userId)
            .show()
    }

    fun userUnBlock() {
        AviroDialogUtils.createTwoDialog(binding.root.context, "더보기", "이 사용자의 후기를 모두 차단하시겠어요?",
            "취소",
            "차단해제") {
             } // viewmodel.userUnBlock(userId) // 차단할 유저 Id
            .show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
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

            getString(R.string.UPDATE_RESULT_OK).toInt() -> {

                if(data != null) {
                    val updateSuccessMsg = data.getStringExtra("updateSuccess")
                    Log.d("가게정보수정","${updateSuccessMsg}")
                    AviroDialogUtils.createOneDialog(
                        requireContext(),
                        "수정 요청이 완료되었어요",
                        "${updateSuccessMsg}",
                        "확인"
                    ).show()

                    // 마커 변경
                    val  updatedCategory = data.getStringExtra("updateCategory") ?: ""
                    if(updatedCategory != "") {
                        mapViewmodel.updateCategory(viewmodel.selectedMarker.value!!.placeId, updatedCategory)
                    }
                }
            }

            getString(R.string.UPDATE_MENU_RESULT_OK).toInt() -> {

                if(data != null) {
                    val updateSuccessMsg = data.getStringExtra("updateSuccess")
                    Log.d("가게메뉴수정","${updateSuccessMsg}")
                    AviroDialogUtils.createOneDialog(
                        requireContext(),
                        "수정 완료되었어요",
                        "${updateSuccessMsg}",
                        "확인"
                    ).show()

                    // 메뉴 리스트 업데이트
                    viewmodel.getRestaurantMenu(viewmodel.selectedMarker.value!!.placeId)

                }
            }

        }
    }
}