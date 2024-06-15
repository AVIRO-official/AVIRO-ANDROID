package com.aviro.android.presentation.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.databinding.FragmentUpdateLocPublicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPublicLocation : Fragment() {

    private var _binding: FragmentUpdateLocPublicBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: UpdateAddressViewModel by viewModels()
    //private val updateViewmodel: UpdateViewModel by activityViewModels()
    //private val viewmodel: UpdateAddressViewModel by activityViewModels()

    //val parentFragment = getParentFragment() as UpdateLocFragment

    lateinit var addressAdapter : PublicAddressAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateLocPublicBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner


        val root: View = binding.root

        addressAdapter = PublicAddressAdapter { item ->
            // 정보 넘김
            val parentFragment = parentFragment as UpdateLocFragment
            parentFragment.setAddrrssData(item)

            viewmodel.getCoordination(item)
        }
        addressAdapter.setViewModel(viewmodel)
        binding.searchRecyclerview.adapter = addressAdapter

        initListener()
        initObserver()

        return root
    }



    fun initObserver() {
        viewmodel.addressItemList.observe(viewLifecycleOwner) {
            binding.guideMessage.visibility = View.GONE

            Log.d("addressItemList","$it")
            if(it == null || it.size == 0) {
                viewmodel._isAddressResult.value = false
            } else {
                viewmodel._isAddressResult.value = true
            }

            (binding.searchRecyclerview.adapter as PublicAddressAdapter).addressList = it?.toList()?.toMutableList()
            (binding.searchRecyclerview.adapter as PublicAddressAdapter).notifyDataSetChanged()
        }

        viewmodel.coordiOfPublicAddress.observe(viewLifecycleOwner) {
            it?.let {
                val parentFragment = parentFragment as UpdateLocFragment
                val fragmentManager = parentFragment.parentFragmentManager.beginTransaction()
                parentFragment.setCoordiData(it.y, it.x)
                parentFragment.setFragmentResult()
                fragmentManager.remove(parentFragment).commit()
            }
        }

        viewmodel.isAddressResult.observe(viewLifecycleOwner) {
            if(!it) {

            }
        }
    }

    fun initListener() {

        binding.searchbarCancleBtn.setOnClickListener {
            binding.EditTextSearchBar.text = null
        }

        binding.searchRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /*
                if(viewmodel.newAddressPage.value!!.totalCount.toInt() != 0 &&
                    viewmodel.newAddressPage.value!!.totalCount.toInt() == viewmodel.currentAmount) return
                 */

                if(viewmodel.totalAmount == viewmodel.currentAmount) return

                // 아직 로딩중인지 확인
                if (!viewmodel.isProgress) { // 아직 로딩중이면 호출 x
                    // 스크롤이 끝에 도달했는지 확인
                    if (!binding.searchRecyclerview.canScrollVertically(1)) { // 더이상 하단으로 내려갈 수 없음
                        viewmodel.searchPublicAddress(viewmodel.searchedKeyword.value!!, viewmodel.currentPage)
                    }
                }
            }
        })


    }



}