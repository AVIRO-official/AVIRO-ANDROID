package com.aviro.android.presentation.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.aviro.android.databinding.FragmentUpdateLocBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UpdateLocFragment : Fragment() {

    private var _binding: FragmentUpdateLocBinding? = null
    private val binding get() = _binding!!

    val frag1 = SearchPublicLocation()
    val frag2 = SearchMapLocation()
    val fragList = arrayOf(frag1, frag2)

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateLocBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()
        initListener()


        return root
    }


    fun initUI() {
        setupViewPager(binding.viewPager, binding.tabLayout)
        binding.viewPager.isUserInputEnabled = false
    }

    fun initListener() {
        binding.backBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@UpdateLocFragment).commit()
        }

    }

    fun setAddrrssData(address : String) {
        bundle.putString("updatedAddress", address)
    }

    fun setCoordiData(latitude:Double, longitude:Double) {
        bundle.putDouble("updatedLocationX", latitude)
        bundle.putDouble("updatedLocationY", longitude)

    }

    fun setFragmentResult() {
        setFragmentResult("resultKey", bundle)
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
                0 -> tab.text = "주소 검색"
                1 -> tab.text = "지도에서 선택"
            }
        }.attach()

    }


}