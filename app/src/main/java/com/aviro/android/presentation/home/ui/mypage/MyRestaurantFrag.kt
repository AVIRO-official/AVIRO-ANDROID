package com.aviro.android.presentation.home.ui.mypage

import android.os.Bundle
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

class MyRestaurantFrag : Fragment() {

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



        initAdapter()
        initListener()
        initObserver()
        initUI()

        return root
    }

    fun initUI() {
        binding.titleTextView.text = "내가 등록한 가게"
        viewmodel._isBookMarkScreen.value = false
    }


    fun initAdapter() {

        adapter = MyRestaurantAdapter(viewmodel) {  item ->
            homeViewmodel._restaurantData.value = item
            homeViewmodel._isNavigation.value = true

            val fragmentManager = parentFragmentManager.beginTransaction()
            //fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
            fragmentManager.remove(this@MyRestaurantFrag).commit()
        }
        binding.recyclerView.adapter = adapter
    }

    fun initListener() {
            binding.backBtn.setOnClickListener {
                homeViewmodel._isNavigation.value = false
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
                setFragmentResult("requestKey", bundleOf("resultKey" to "MypageToChallenge"))
                fragmentManager.remove(this@MyRestaurantFrag).commit()
                //parentFragmentManager.popBackStack()
            }

        binding.gotoBtn.setOnClickListener {
            homeViewmodel._isNavigation.value = true
        }


    }

    fun initObserver() {
        viewmodel.myRestaurantList.observe(viewLifecycleOwner) {
            if(it.isEmpty()) {
                binding.noListContainer.visibility = View.VISIBLE
                binding.listContainer.visibility = View.GONE
                binding.noListTextView.text = "아직 등록한 가게가 없습니다\n" + "지금 가게를 등록하러 가볼까요?"
                binding.addMyActivityBtnTextView.text = "지금 가게 등록하기"

                homeViewmodel._currentNavigation.value = HomeViewModel.WhereToGo.REGISTER
            } else {
                binding.noListContainer.visibility = View.GONE
                binding.listContainer.visibility = View.VISIBLE
                adapter.setData(it as MutableList<MyRestaurant>)
                binding.countTextView.text = "총 ${viewmodel.myRestaurantList.value!!.size}개의 가게"

                homeViewmodel._currentNavigation.value = HomeViewModel.WhereToGo.RESTAURANT
            }

            /*
            it?.let {
                binding.noListContainer.visibility = View.GONE
                binding.listContainer.visibility = View.VISIBLE
                adapter.setData(it as MutableList<MyRestaurant>)
                binding.countTextView.text = "총 ${viewmodel.myRestaurantList.value!!.size}개의 가게"

            }.run {
                binding.noListContainer.visibility = View.VISIBLE
                binding.listContainer.visibility = View.GONE
                binding.noListTextView.text = "아직 등록한 가게가 없습니다\n" + "지금 가게를 등록하러 가볼까요?"
                binding.addMyActivityBtnTextView.text = "지금 가게 등록하기"
            }
             */
        }

    }
}