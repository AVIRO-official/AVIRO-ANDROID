package com.aviro.android.presentation.update

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.aviro.android.databinding.ActivityUpdateBinding
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Update : FragmentActivity() {

    private lateinit var binding : ActivityUpdateBinding
    private val viewmodel : UpdateViewModel by viewModels()

    val frag1 = UpdateInfoFragment()
    val frag2 = UpdateNumberFragment()
    val frag3 = UpdateTimetableFragment()
    val frag4 = UpdateHomepageFragment()
    val fragList = arrayOf(frag1, frag2, frag3, frag4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        setupViewPager(binding.viewPager, binding.tabLayout)
        initObserver()
        initListener()
        initData()
    }

    fun initObserver() {
        viewmodel.error.observe(this) {
            it?.let { errorMsg ->
                AviroDialogUtils.createOneDialog(
                    this,
                    "Error",
                    "${it}",
                    "확인"
                ).show()
            }
        }

        viewmodel.toast.observe(this) {
            it?.let { successMsg ->
                intent.putExtra("updateSuccess", successMsg)
                intent.putExtra("updateCategory", viewmodel.afterInfoData.value!!.category)
                Log.d("updateCategory", "${ viewmodel.afterInfoData.value!!.category}")
                setResult(Activity.RESULT_OK, intent)

                finish()
            }

        }
    }

    fun initListener() {

        binding.updateBtn.setOnClickListener {
            viewmodel.updateRestaurantData()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }




    }

    fun initData() {
        // 데이터 추출
        val type = intent.getStringExtra("NaviType")
        when(type) {

            "LOC" ->  {
                binding.viewPager.setCurrentItem(0, false)
                binding.viewPager.isUserInputEnabled = false
            }
            "HOMEPAGE" -> {
                binding.viewPager.setCurrentItem(3, false)
                binding.viewPager.isUserInputEnabled = false
            }
            "NUMBER" -> {
                binding.viewPager.setCurrentItem(1, false)
                binding.viewPager.isUserInputEnabled = false
            }
            "TIME" -> {
                binding.viewPager.setCurrentItem(2, false)
                binding.viewPager.isUserInputEnabled = false
            }
        }

        viewmodel._restaurantInfo.value = intent.getParcelableExtra("RestaurantInfo")
        viewmodel._restaurantTimetable.value = intent.getParcelableExtra("RestaurantTimeTable")

        viewmodel.initData()

    }

    private fun setupViewPager(viewPager: ViewPager2, tabLayout: TabLayout) {
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {
                fragList[position]
                return fragList[position]
            }
        }

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "위치"
                1 -> tab.text = "전화번호"
                2 -> tab.text = "영업시간"
                3 -> tab.text = "홈페이지"
            }
        }.attach()
    }

    fun addNewFragment(fragment: Fragment) {
       supportFragmentManager.beginTransaction()
        .add(binding.updateContainer.id, fragment)
        .addToBackStack(null)
        .commit()
    }




}