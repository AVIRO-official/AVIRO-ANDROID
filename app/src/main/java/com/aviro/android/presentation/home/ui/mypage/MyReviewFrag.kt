package com.aviro.android.presentation.home.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.aviro.android.R
import com.aviro.android.databinding.MypageListFragBinding
import com.aviro.android.domain.entity.member.MyComment
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.home.HomeViewModel

class MyReviewFrag : Fragment() {

    private var _binding: MypageListFragBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: MypageViewModel by activityViewModels()
    private val homeViewmodel: HomeViewModel by activityViewModels()

    private lateinit var adapter : MyReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MypageListFragBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        homeViewmodel._currentNavigation.value = HomeViewModel.WhereToGo.REVIEW

        initAdapter()
        initListener()
        initObserver()

        binding.titleTextView.text = "내가 작성한 후기"


        return root
    }



    fun initAdapter() {
        adapter = MyReviewAdapter( {  item ->
            // 리뷰 화면으로 이동
            homeViewmodel._reviewData.value = item
            homeViewmodel._isNavigation.value = true

            val fragmentManager = parentFragmentManager.beginTransaction()
            //fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
            fragmentManager.remove(this@MyReviewFrag).commit()
        },
            { item ->
            // 리뷰 삭제
          viewmodel.deleteReview(item) }
        )

        binding.recyclerView.adapter = adapter
    }

    fun initListener() {
        binding.backBtn.setOnClickListener {
            homeViewmodel._isNavigation.value = false

            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
            fragmentManager.remove(this@MyReviewFrag).commit()

        }

        binding.gotoBtn.setOnClickListener {
            homeViewmodel._isNavigation.value = true
        }

    }

    fun initObserver() {

        viewmodel.myReviewList.observe(viewLifecycleOwner) {
            if(it.isEmpty()) {
                binding.noListContainer.visibility = View.VISIBLE
                binding.listContainer.visibility = View.GONE

                binding.noListTextView.text = "아직 작성한 후기가 없습니다\n" + "${viewmodel.nickname.value}님의 후기를 들려주실래요?"
                binding.addMyActivityBtnTextView.text = "지금 후기 작성하기"
            } else {
                binding.noListContainer.visibility = View.GONE
                binding.listContainer.visibility = View.VISIBLE

                adapter.setData(it as MutableList<MyComment>)
                binding.countTextView.text = "총 ${viewmodel.myReviewList.value!!.size}개의 후기"
            }

        }

        viewmodel.toastLiveData.observe(viewLifecycleOwner) {
            it?.let {
                val toast = Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                toast.show()
                viewmodel.getMyReviewList()
                viewmodel._toastLiveData.value = null
            }
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

        /*
        viewmodel.isMyList.observe(viewLifecycleOwner) {
            Log.d("MyReviewFrag","${it}")
            if(!it) { // 리스트 존재
                binding.noListTextView.text = "아직 작성한 후기가 없습니다 + \n + ${viewmodel.nickname}님의 후기를 들려주실래요?"
                binding.addMyActivityBtnTextView.text = "지금 후기 작성하기"
            } else {
                binding.countTextView.text = "총 ${viewmodel.myReviewList.value!!.size}개의 후기"
            }
        }
         */

    }
}