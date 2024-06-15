package com.aviro.android.presentation.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.aviro.android.R
import com.aviro.android.common.getSelectedMarkerIcon
import com.aviro.android.databinding.FragmentUpdateInfoBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class UpdateInfoFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentUpdateInfoBinding? = null
    private val binding get() = _binding!!
    private val viewmodel : UpdateViewModel by activityViewModels()

    private lateinit var mapFragment : MapFragment
    private var naverMap : NaverMap? = null
    lateinit var marker : Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = childFragmentManager
        mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
        //binding.mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateInfoBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        val root: View = binding.root


        initListener()

        return root
    }


    fun initListener() {

        binding.updtaeAdressBtn.setOnClickListener {
            // 주소 상세 설정 화면으로 이동
            if (activity is Update) {
                val activity: Update? = activity as Update?
                val newFragment = UpdateLocFragment()
                activity!!.addNewFragment(newFragment)
            }

            setFragmentResultListener("resultKey") { requestKey, bundle ->
                val address = bundle.getString("updatedAddress")
                address?.let{
                    val latitude = bundle.getDouble("updatedLocationX")
                    val longitude = bundle.getDouble("updatedLocationY")

                    // 지도 중심점 변경
                    naverMap!!.cameraPosition = CameraPosition(LatLng(latitude, longitude),
                        18.0, 0.0, 0.0
                    )
                    marker.map = null
                    marker = Marker(LatLng(latitude, longitude)) // 마커 위치 변경
                    marker.map = naverMap
                    marker.icon =  getSelectedMarkerIcon(
                        viewmodel.restaurantInfo.value!!.category,
                        viewmodel.restaurantInfo.value!!.allVegan,
                        viewmodel.restaurantInfo.value!!.someMenuVegan,
                        viewmodel.restaurantInfo.value!!.ifRequestVegan)

                    viewmodel._afterInfoData.value = viewmodel._afterInfoData.value!!.copy(x = latitude , y = longitude, address = address)
                    viewmodel.checkChangedInfo()
                }
            }
        }

    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.uiSettings.isScrollGesturesEnabled = false // 스크롤 불가
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isZoomGesturesEnabled = false// 줌인아웃 불가
        naverMap.uiSettings.isCompassEnabled = false // 나침반 없애기
        naverMap.uiSettings.isTiltGesturesEnabled = false

        naverMap.cameraPosition = CameraPosition(LatLng(viewmodel.restaurantInfo.value!!.x, viewmodel.restaurantInfo.value!!.y),
            18.0, 0.0, 0.0
        )
        marker = Marker(LatLng(viewmodel.restaurantInfo.value!!.x, viewmodel.restaurantInfo.value!!.y))
        //val marker = Marker(LatLng(viewmodel.restaurantInfo.value!!.x, viewmodel.restaurantInfo.value!!.y))
        marker.map = naverMap
        marker.icon =  getSelectedMarkerIcon(
            viewmodel.restaurantInfo.value!!.category,
            viewmodel.restaurantInfo.value!!.allVegan,
            viewmodel.restaurantInfo.value!!.someMenuVegan,
            viewmodel.restaurantInfo.value!!.ifRequestVegan)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        naverMap = null
    }
}