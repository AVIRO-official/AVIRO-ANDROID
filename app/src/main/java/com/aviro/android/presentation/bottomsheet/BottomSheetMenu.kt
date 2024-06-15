
package com.aviro.android.presentation.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aviro.android.R
import com.aviro.android.databinding.FragmentBottomsheetMenuBinding
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.home.ui.map.MapViewModel
import com.aviro.android.presentation.update.UpdateMenu


class BottomSheetMenu : Fragment() {

    private lateinit var mapViewmodel: MapViewModel
    private lateinit var viewmodel: BottomSheetViewModel
    private var _binding: FragmentBottomsheetMenuBinding? = null
    private val binding get() = _binding!!
    val menuAdapter = MenuAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentBottomsheetMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = this
        binding.menuListView.adapter = menuAdapter
        initObserver()
        initListener()

        return root
    }

    fun setViewModel(bottomSheetViewModel: BottomSheetViewModel, mapViewModel: MapViewModel) {
        this.viewmodel = bottomSheetViewModel
        this.mapViewmodel = mapViewModel
    }


    fun initObserver() {
        viewmodel.menuList.observe(viewLifecycleOwner) {
            binding.menuListView.removeAllViews()
            binding.restaurantMenu = viewmodel.menuList.value
            (binding.menuListView.adapter as MenuAdapter).menuList = it.menuArray.toMutableList()
            //(binding.menuListView.adapter as MenuAdapter).notifyDataSetChanged()
        }
    }

    fun initListener() {
        binding.menuUpdateBtn.setOnClickListener {
            // 메뉴 업데이트 화면
            viewmodel.setRestaurantData(mapViewmodel.selectedMarker.value!!)
            val intent = Intent(requireContext(), UpdateMenu::class.java)
            intent.putExtra("RestaurantInfo", viewmodel.restaurantDataForUpdate.value)
            startActivityForResult(intent, getString(R.string.UPDATE_MENU_RESULT_OK).toInt())
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            getString(R.string.UPDATE_MENU_RESULT_OK).toInt() -> {

                if(data != null) {
                    val updateSuccessMsg = data.getStringExtra("updateSuccess")

                    Log.d("가게메뉴수정", "${updateSuccessMsg}")
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