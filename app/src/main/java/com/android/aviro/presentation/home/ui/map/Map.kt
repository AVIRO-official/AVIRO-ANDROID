package com.android.aviro.presentation.home.ui.map

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.android.aviro.BuildConfig.NAVER_CLIENT_ID
import com.android.aviro.R
import com.android.aviro.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource


class Map : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: MapViewModel by hiltNavGraphViewModels(R.id.navigation_map)
    private lateinit var locationSource: FusedLocationSource

    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    val GPS_ENABLE_REQUEST_CODE = 1000
    val PERMISSIONS_REQUEST_CODE = 2000
    val LOCATION_PERMISSION_REQUEST_CODE = 3000


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentMapBinding.inflate(inflater, container, false)
            val root: View = binding.root
            binding.viewmodel = viewmodel
            binding.lifecycleOwner = this

            NaverMapSdk.getInstance(requireContext()).client =
                NaverMapSdk.NaverCloudPlatformClient(NAVER_CLIENT_ID)

            checkPermission()

            return root
        }

    fun initMap() {

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        locationSource =
            FusedLocationSource(this, PERMISSIONS_REQUEST_CODE)

        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        val locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
             ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)  != PackageManager.PERMISSION_GRANTED ) {

            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val cameraCurrentPosition = CameraPosition(LatLng(currentLocation!!.longitude, currentLocation.latitude),16.0)
            naverMap.setCameraPosition(cameraCurrentPosition)
        }


        naverMap.uiSettings.isZoomControlEnabled = false

        // 위치 추적 설정
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 현재 위치로 돌아오기
        binding.locationFloatingButton.setOnClickListener {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }

        viewmodel.drawMarker()

        viewmodel.markerList.observe(this, androidx.lifecycle.Observer {

            Log.d("markerList","${it.markerList}")
            it.markerList.forEach { makerEntity ->
                val marker = makerEntity.marker as Marker
                marker.map = naverMap
            }


        })


    }


    fun checkPermission() {
        if (checkOnOffGPS()) {
            if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
                context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED ) {
                // 이전에 퍼미션 거부한 적이 있는 경우
                if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission_list[0]) } == true) {
                    Toast.makeText(context, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                }
                requestPermissions(permission_list, 0)
            } else {
                // 권한 허용
                initMap()
            }
        } else {
            setGPS()
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkOnOffGPS(): Boolean {
        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun setGPS() {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("GPS 비활성화")
            builder.setMessage("GPS를 켜져 있어야 비건맵 서비스를 이용할 수 있습니다.".trimIndent())
            builder.setCancelable(true)
            builder.setPositiveButton("설정") { dialog, id ->
                val callGPSSettingIntent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            }
            builder.setNegativeButton(
                "취소"
            ) { dialog, id -> dialog.cancel() }
            builder.create().show()

    }

    // GPS 권한 설정 콜백
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                checkPermission()
        }
    }

    // 위치 권한 설정 콜백
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                var isCheck = true
                for (isGranted in grantResults) {
                    if (isGranted == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(context, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show()
                        // 권한 거절시 다시 물어봄
                        val builder = AlertDialog.Builder(context!!)
                        builder.setMessage("퍼미션이 거부되었습니다.\n맵 서비스를 사용하시려면 앱 설정에서 위치 권한을 허용해주세요.")
                        builder.setPositiveButton("확인") { dialog, which ->
                        }
                        builder.show()

                        isCheck = false

                        break
                    }
                }

                if(isCheck) {


                }
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}