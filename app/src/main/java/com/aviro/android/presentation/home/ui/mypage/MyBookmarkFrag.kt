package com.aviro.android.presentation.home.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.aviro.android.R

import com.aviro.android.databinding.MypageListFragBinding
import com.aviro.android.domain.entity.member.MyRestaurant
import com.aviro.android.presentation.home.HomeViewModel

class MyBookmarkFrag : Fragment() {

    private var _binding: MypageListFragBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: MypageViewModel by activityViewModels()
    private val homeViewmodel: HomeViewModel by activityViewModels()

    private lateinit var adapter : MyRestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = MypageListFragBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        homeViewmodel._currentNavigation.value = HomeViewModel.WhereToGo.BOOKMARK

        initUI()
        initAdapter()
        initListener()
        initObserver()

        return root
    }

    fun initUI() {
        binding.titleTextView.text = "즐겨찾기"
        viewmodel._isBookMarkScreen.value = true
    }

    fun initAdapter() {
        adapter = MyRestaurantAdapter(viewmodel) {  item ->
            homeViewmodel._restaurantData.value = item
            Log.d("MyBookmarkFrag","${homeViewmodel._restaurantData.value}")
            homeViewmodel._isNavigation.value = true

            val fragmentManager = parentFragmentManager.beginTransaction()
            //fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
            fragmentManager.remove(this@MyBookmarkFrag).commit()

        }
        binding.recyclerView.adapter = adapter
    }

    fun initListener() {

        binding.backBtn.setOnClickListener {
            homeViewmodel._isNavigation.value = false
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
            fragmentManager.remove(this@MyBookmarkFrag).commit()
        }

        binding.gotoBtn.setOnClickListener {
            homeViewmodel._isNavigation.value = true
        }
    }

    fun initObserver() {

        viewmodel.myBookmarkList.observe(viewLifecycleOwner) {
            if(it.isEmpty()) {
                binding.noListContainer.visibility = View.VISIBLE
                binding.listContainer.visibility = View.GONE

                binding.noListTextView.text = "아직 즐겨찾기한 가게가 없습니다\n" + "지금 가게를 둘러볼까요?"
                binding.addMyActivityBtnTextView.text = "홈으로 이동하기"
            } else {
                binding.noListContainer.visibility = View.GONE
                binding.listContainer.visibility = View.VISIBLE

                adapter.setData(it as MutableList<MyRestaurant>)
                binding.countTextView.text = "총 ${viewmodel.myBookmarkList.value!!.size}개의 가게"
            }

            /*
            it?.let {
                binding.noListContainer.visibility = View.GONE
                binding.listContainer.visibility = View.VISIBLE

                adapter.setData(it as MutableList<MyRestaurant>)
                binding.countTextView.text = "총 ${viewmodel.myBookmarkList.value!!.size}개의 가게"

            }.run {
                binding.noListContainer.visibility = View.VISIBLE
                binding.listContainer.visibility = View.GONE

                binding.noListTextView.text = "아직 즐겨찾기한 가게가 없습니다\n" + "지금 가게를 둘러볼까요?"
                binding.addMyActivityBtnTextView.text = "홈으로 이동하기"
            }

             */
        }

        /*
        viewmodel.isMyList.observe(viewLifecycleOwner) {
            if(!it) { // 리스트 없음
                binding.noListTextView.text = "아직 즐겨찾기한 가게가 없습니다 + \n + 지금 가게를 둘러볼까요?"
                binding.addMyActivityBtnTextView.text = "홈으로 이동하기"
            } else {
                binding.countTextView.text = "총 ${viewmodel.myBookmarkList.value!!.size}개의 가게"
            }
        }

         */

    }

}