package com.aviro.android.presentation.update

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.R
import com.aviro.android.common.getMarkerPin
import com.aviro.android.databinding.FragmentUpdateLocMapBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class SearchMapLocation : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentUpdateLocMapBinding? = null
    private val binding get() = _binding!!

    private val addressViewmodel: UpdateAddressViewModel by activityViewModels()
    private val viewmodel: UpdateViewModel by activityViewModels()

    //val parentFragment = getParentFragment() as UpdateLocFragment

    private lateinit var mapFragment : MapFragment
    private var naverMap : NaverMap? = null
    private lateinit var locationSource: FusedLocationSource


    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = parentFragmentManager
        mapFragment = fm.findFragmentById(R.id.search_map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.search_map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateLocMapBinding.inflate(inflater, container, false)
        //binding.viewmodel = viewmodel
        //binding.lifecycleOwner = viewLifecycleOwner
        val root: View = binding.root

        initListener()
        initObserver()
        return root

    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        locationSource = FusedLocationSource(this, getString(R.string.FUSED_LOCATION_CODE).toInt())
        naverMap.uiSettings.isCompassEnabled = false
        naverMap.uiSettings.isZoomGesturesEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.isTiltGesturesEnabled = false

        naverMap.cameraPosition = CameraPosition(
            LatLng(viewmodel.restaurantInfo.value!!.x, viewmodel.restaurantInfo.value!!.y),
            18.0, 0.0, 0.0
        )


       binding.pinImageView.background = getMarkerPin(
           requireContext(),
            viewmodel.restaurantInfo.value!!.category,
            viewmodel.restaurantInfo.value!!.allVegan,
            viewmodel.restaurantInfo.value!!.someMenuVegan,
            viewmodel.restaurantInfo.value!!.ifRequestVegan)



        // 마커를 스크롤하면 주소 바뀜 -> 주소 텍스트뷰 변경
        // 주소가 있는 경우에만 등록 버튼 활성화
        naverMap.addOnCameraChangeListener { reason, animated ->
            val cameraPosition = naverMap.cameraPosition

            val parentFragment = parentFragment as UpdateLocFragment
            parentFragment.setCoordiData(cameraPosition.target.latitude, cameraPosition.target.longitude)

            // 중심점 좌표를 기준으로 주소 구함
            addressViewmodel.getRoadAddress(cameraPosition.target.latitude, cameraPosition.target.longitude)

            // 주소가 있는 경우
            //setUpdateBtn()
        }

    }

    fun initListener() {
        // 위치 추적 버튼 클릭
        binding.locationFloatingButton.setOnClickListener {
            getCurrentUserLocation()
        }

        binding.addressUpdateBtn.setOnClickListener {
            val parentFragment = parentFragment as UpdateLocFragment
            val fragmentManager  = parentFragment.parentFragmentManager.beginTransaction()
            //fragmentManager.setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_right_exit, R.anim.slide_right_enter, R.anim.slide_left_exit)
            fragmentManager.remove(parentFragment).commit()
        }

    }

    fun initObserver() {
        addressViewmodel.addressOfMap.observe(viewLifecycleOwner) {
            it?.let {
                binding.addressUpdateBtn.isEnabled = true
                binding.addressUpdateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.base_roundsquare_cobalt_10)
                binding.addressUpdateBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray7))

                val parentFragment = parentFragment as UpdateLocFragment
                parentFragment.setAddrrssData(it)
                parentFragment.setFragmentResult()

                binding.addressTextView.text = it

            } ?: run {
                binding.addressUpdateBtn.isEnabled = false
                binding.addressUpdateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.base_roundsquare_gray6)
                binding.addressUpdateBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray3))

                binding.addressTextView.text = null

            }
        }
    }



    fun getCurrentUserLocation() {
        checkOnOffGPS()
    }

    private fun checkOnOffGPS(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 위치 추적을 위한 위치 정보 권한 확인
            requestPermissions(permission_list, getString(R.string.PERMISSIONS_REQUEST_CODE).toInt())
        } else {
            settingGPS()
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // gps 셋팅
    fun settingGPS() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("GPS 비활성화")
            .setMessage("GPS를 켜져 있어야 비건맵 서비스를 이용할 수 있습니다.".trimIndent())
            .setCancelable(true)
            .setPositiveButton("설정하기") { dialog, id ->
                val callGPSSettingIntent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, getString(R.string.GPS_ENABLE_REQUEST_CODE).toInt())
                // GPS 설정하러 설정 화면 넘어감
            }
            .setNegativeButton("취소") { dialog, id ->
                dialog.cancel()
            }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            // 위치 추적 기능 사용
            getString(R.string.FUSED_LOCATION_CODE).toInt() -> {
                // 위치 퍼미션 허용 안 함
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    //퍼미션 요청
                    requestPermissions(
                        permission_list,
                        getString(R.string.PERMISSIONS_REQUEST_CODE).toInt()
                    )
                } else {
                    // 위치 추적 설정
                    naverMap!!.locationSource = locationSource
                    naverMap!!.locationTrackingMode = LocationTrackingMode.Follow
                }

            }
            // 위치 퍼미션 사용
            getString(R.string.PERMISSIONS_REQUEST_CODE).toInt() -> {
                Log.d("위치 퍼미션 콜백", "위치 퍼미션 콜백")
                var check = true
                for (isGranted in grantResults) {
                    if (isGranted == PackageManager.PERMISSION_DENIED) {
                        check = false
                        // 권한 거절시 Dialog로 다시 물어봄
                        MaterialAlertDialogBuilder(
                            ContextThemeWrapper(requireContext(), R.style.Base_Theme_AVIRO)
                        )
                            .setTitle("위치 정보 액세스 권한 설정")
                            .setMessage("위치정보 이용에 대한 액세스 권한이 없어요\n앱 설정으로 가서 액세스 권한을 수정할 수 있어요. 이동하시겠어요?")
                            .setNegativeButton("취소") { dialog, which ->
                                // 권한 설정 거부
                                // 위치 추적 없이 구동
                                dialog.cancel()

                            }
                            .setPositiveButton("설정하기") { dialog, which ->
                                // 권한 설정 화면 이동
                                val callLocPermissonSettingIntent =
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", "com.aviro.android", null)
                                    )
                                callLocPermissonSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(callLocPermissonSettingIntent)
                                dialog.cancel()
                            }
                            .show()
                    }
                }
                if (check) {
                    Log.d("위치 추적", "위치추적")
                    naverMap!!.locationSource = locationSource
                    naverMap!!.locationTrackingMode = LocationTrackingMode.Follow
                }
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        naverMap = null
    }

}