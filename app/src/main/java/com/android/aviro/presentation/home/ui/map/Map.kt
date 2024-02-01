package com.android.aviro.presentation.home.ui.map

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.R
import com.android.aviro.databinding.FragmentMapBinding
import com.android.aviro.presentation.home.ui.register.OnSwipeTouchListener
import com.android.aviro.presentation.search.Search
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Map : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    //private val viewmodel: MapViewModel by hiltNavGraphViewModels(R.id.navigation_map)
    private val viewmodel: MapViewModel by viewModels()

    private lateinit var locationSource: FusedLocationSource

    private lateinit var mapFragment : MapFragment

    //private lateinit var mapView : MapView
    private var naver_map : NaverMap? = null

    //lateinit var bottomSheet : LinearLayout
    //lateinit var persistenetBottomSheet: BottomSheetBehavior<View>
    private lateinit var gestureDetector: GestureDetector
    private var isTouching = false
    var step  = 0

    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    val GPS_ENABLE_REQUEST_CODE = 1000
    val PERMISSIONS_REQUEST_CODE = 2000
    val FUSED_LOCATION_CODE = 3000

    override fun onAttach(context: Context) {
        Log.d("프래그먼트 생명주기","onAttach")
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("프래그먼트 생명주기","onCreate")
        super.onCreate(savedInstanceState)

        // 프래그먼트가 액티비티의 호출을 받아 생성
        // 네이버 지도 초기화 (navMap 객체 생성, 기본 가게데이터 생성) -> 퍼미션 없이도 사용 가능한 기능

        val fm = childFragmentManager
        mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("프래그먼트 생명주기","onCreateView")

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        val bottomSheet = binding.bottomSheetLayout
        val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
        persistenetBottomSheet.state = STATE_HIDDEN
        gestureDetector = GestureDetector(context, GestureListener())

        binding.bottomSheetLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }


    viewmodel.selectedMarker.observe(this, androidx.lifecycle.Observer {
            viewmodel._isShowBottomSheetTab.value = false
            persistenetBottomSheet.state = STATE_COLLAPSED
            step = 1
        })


        return root
    }

    override fun onResume() {
        Log.d("프래그먼트 생명주기","onResume")
        super.onResume()
        //checkOnOffGPS()
        if(viewmodel.isFirst.value == false && naver_map != null) {
            viewmodel.updateMap(naver_map!!, false)
        }
        viewmodel._isFirst.value = false

    }



    // 지도만 준비되면 퍼미션과 상관없이 동작
    //@UiThread
    override fun onMapReady(naverMap: NaverMap) {

        naver_map = naverMap

        // 위치 추적 기능 사용 여부 확인 (퍼미션 체크 이루어짐)
        locationSource =
            FusedLocationSource(this, FUSED_LOCATION_CODE)

        // GPS 체크 및 퍼미션 체크 (한번허용, 항상 허용, 거부)
        checkOnOffGPS()

        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.setScaleBarEnabled(false)

        // 맵 객체가 다시 생길 때 화면에 마커를 모두 다시 그림
        viewmodel.updateMap(naverMap, true)
        viewmodel._isFirst.value = true // onResume에서 다시 그려짐 방지

        binding.locationFloatingButton.setOnClickListener {
            //naverMap.locationTrackingMode = LocationTrackingMode.Follow
            //binding.locationFloatingButton.setBackgroundResource(R.drawable.ic_floating_location)
            checkOnOffGPS()
        }


        binding.searchBar.setOnClickListener {
            val searchIntent = Intent(requireContext(), Search::class.java)
            val centerPostion = naver_map!!.cameraPosition
            val centerLatLng = centerPostion.target
            searchIntent.putExtra("NaverMapOfX", centerLatLng.longitude)
            searchIntent.putExtra("NaverMapOfY",  centerLatLng.latitude)
            startActivity(searchIntent)


        }


    }



    // GPS가 켜져있는지 확인
    private fun checkOnOffGPS(): Boolean {
        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // gps 켜져 있음
            // 위치 추적을 위한 위치 정보 권한 확인
            requestPermissions(permission_list, PERMISSIONS_REQUEST_CODE)
        } else {
            settingGPS()
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun settingGPS() {
        MaterialAlertDialogBuilder(context!!)
            .setTitle("GPS 비활성화")
            .setMessage("GPS를 켜져 있어야 비건맵 서비스를 이용할 수 있습니다.".trimIndent())
            .setCancelable(true)
            .setPositiveButton("설정하기") { dialog, id ->
                val callGPSSettingIntent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
                // GPS 설정하러 설정 화면 넘어감
            }
            .setNegativeButton("취소") { dialog, id ->
                //
                dialog.cancel()
            }
            .show()

    }

    // GPS 권한 설정 콜백
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE -> {
                //사용자가 GPS 활성 시켰는지 검사
                requestPermissions(permission_list, PERMISSIONS_REQUEST_CODE)
            }
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
            // 위치 추적 기능 사용
            FUSED_LOCATION_CODE -> {
                // 위치 퍼미션 허용 안 함
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    //퍼미션 요청
                    requestPermissions(permission_list, PERMISSIONS_REQUEST_CODE)
                } else {
                    // 위치 추적 설정
                    naver_map!!.locationSource = locationSource
                    naver_map!!.locationTrackingMode = LocationTrackingMode.Follow
                }

            }
            // 위치 퍼미션 사용
            PERMISSIONS_REQUEST_CODE -> {
                Log.d("위치 퍼미션 콜백","위치 퍼미션 콜백")
                var check = true
                for (isGranted in grantResults) {
                    if (isGranted == PackageManager.PERMISSION_DENIED) {
                        check = false
                         // 권한 거절시 Dialog로 다시 물어봄
                        MaterialAlertDialogBuilder(
                            ContextThemeWrapper(requireContext(), R.style.AVIRO_Dialog))
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
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", "com.android.aviro", null))
                                callLocPermissonSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(callLocPermissonSettingIntent)
                                dialog.cancel()
                            }
                            .show()
                    }
                }
                if(check) {
                    Log.d("위치 추적","위치추적")
                    naver_map!!.locationSource = locationSource
                    naver_map!!.locationTrackingMode = LocationTrackingMode.Follow
                }

            }


        }

    }

    override fun onPause() {
        Log.d("프래그먼트 생명주기","onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("프래그먼트 생명주기","onStop")
        super.onStop()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("프래그먼트 생명주기", "onDestroyView")
        _binding = null
        naver_map = null
    }

    override fun onDestroy() {
        Log.d("프래그먼트 생명주기", "onDestroy")
        super.onDestroy()


    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            // 반응성 조절
            private val SWIPE_DISTANCE_THRESHOLD = 150
            private val SWIPE_VELOCITY_THRESHOLD = 150

        override fun onDown(e: MotionEvent): Boolean {
            if(step == 2) {
                Log.d("onFling","3단계")
                val bottomSheet = binding.bottomSheetLayout
                val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
                persistenetBottomSheet.state = STATE_EXPANDED
                step = 3
            }
            return true
        }
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            Log.d("onFling","스와이프")
            val distanceX = e2.x - e1!!.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) < Math.abs(distanceY)
                && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD
                && Math.abs(distanceY) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceY < 0) {
                    if (step == 1) {
                        Log.d("onFling","2단계")
                        viewmodel._isShowBottomSheetTab.value = true
                        val bottomSheet = binding.bottomSheetLayout
                        val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
                        val density = resources.displayMetrics.density

                        // 초기 peek height 설정
                        val initialPeekHeight = persistenetBottomSheet.peekHeight
                        // 변경하고자 하는 높이
                        val targetPeekHeight = (450 * density).toInt()

                        ValueAnimator.ofInt(initialPeekHeight, targetPeekHeight).apply {
                            duration = 200 // 애니메이션 지속 시간(ms) 설정

                            addUpdateListener { animator ->
                                val animatedValue = animator.animatedValue as Int
                                persistenetBottomSheet.setPeekHeight(animatedValue)
                                bottomSheet.requestLayout()
                            }
                            start()
                        }
                        step = 2
                    }
                }
                return true
            }
            return false
        }
    }



}

