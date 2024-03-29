package com.android.aviro.presentation.home.ui.map

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
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
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.R
import com.android.aviro.databinding.FragmentMapBinding
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.presentation.bottomsheet.BottomSheetHome
import com.android.aviro.presentation.bottomsheet.BottomSheetMenu
import com.android.aviro.presentation.bottomsheet.BottomSheetReview
import com.android.aviro.presentation.home.ui.mypage.ChallengeFragment
import com.android.aviro.presentation.home.ui.register.RegisterFragment
import com.android.aviro.presentation.search.Search
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
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

    private val TOUCH_THRESHOLD = 10
    private var startX = 0f
    private var startY = 0f

    val maxOffset = 450
    val minOffset = 150

    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val frag1 = BottomSheetHome()
    val frag2 = BottomSheetMenu()
    val frag3 = BottomSheetReview()

    val fragList = arrayOf(frag1, frag2, frag3)

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("프래그먼트 생명주기","onCreateView")

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this
        binding.bottomSheet.viewmodel = viewmodel
        binding.bottomSheet.fragmentBottomsheetStep2.viewmodel = viewmodel
        setupViewPager(binding.bottomSheet.fragmentBottomsheetStep2.viewPager, binding.bottomSheet.fragmentBottomsheetStep2.tabLayout)


        val bottomSheet = binding.bottomSheetLayout
        // bottomSheet로부터 bottomSheet 동작을 제어할 수 있는 Behavior 추출
        val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)

        persistenetBottomSheet.state = STATE_HIDDEN
        binding.mapFragment.setBottomSheetBehavior(persistenetBottomSheet)
        binding.mapFragment.setViewModel(viewmodel)
        gestureDetector = GestureDetector(context, GestureListener())

        binding.bottomSheetLayout.setOnTouchListener { _, event ->
            // 뷰그룹의 onTouchEvent() 호출 (즉, intercpt 에서 true한 효과와 동일)
            gestureDetector.onTouchEvent(event)
        }

        persistenetBottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                /*
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    // 사용자가 드래그하여 바텀 시트가 최대 위치에 도달할 때
                    persistenetBottomSheet.peekHeight = maxOffset // 최대 위치 설정
                }
                 */
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 바텀 시트가 슬라이드될 때 호출되는 콜백
                //if(persistenetBottomSheet.current)
            }
        })

        binding.bottomSheet.backBtn.setOnClickListener {
            val density = resources.displayMetrics.density
            persistenetBottomSheet.peekHeight = (150 * density).toInt()
            persistenetBottomSheet.state = STATE_COLLAPSED

            viewmodel._BottomSheetStep1.value = true
            viewmodel._BottomSheetState.value = 1
        }

        viewmodel.selectedMarker.observe(this, androidx.lifecycle.Observer {
            // 검색바 텍스트 설정
            if(it == null){
                // 이전 마커 원래대로

                binding.searchbarTextView.text = "어디로 이동할까요?"
                binding.searchbarTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray3))
            }
            else {
                Log.d("selectedMarker", "마커가 선택되었습니다")
                viewmodel.getRestaurantSummary()
                // 바텀시트 UP
                viewmodel._isShowBottomSheetTab.value = true
                persistenetBottomSheet.state = STATE_COLLAPSED
                viewmodel._BottomSheetStep1.value = true
                viewmodel._BottomSheetState.value = 1
                // 이전 마커 원래대로

            }
        })

        binding.actionDownFloatingButton.setOnClickListener {
            val bottomSheet = binding.bottomSheetLayout
            val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
            //persistenetBottomSheet.state = STATE_COLLAPSED
            val density = resources.displayMetrics.density
            persistenetBottomSheet.peekHeight = (150 * density).toInt()
            persistenetBottomSheet.state = STATE_COLLAPSED

            /*
            // 초기 peek height 설정
            val initialPeekHeight = persistenetBottomSheet.peekHeight
            // 변경하고자 하는 높이
            val targetPeekHeight = (150 * density).toInt()

            ValueAnimator.ofInt(initialPeekHeight, targetPeekHeight).apply {
                duration = 100
                interpolator = AccelerateDecelerateInterpolator()

                addUpdateListener { animator ->
                    val animatedValue = animator.animatedValue as Int
                    persistenetBottomSheet.setPeekHeight(animatedValue)
                    bottomSheet.requestLayout()
                }
                start()
            }

             */

            viewmodel._BottomSheetStep1.value = true
            viewmodel._BottomSheetState.value = 1
        }

        viewmodel.selectedItemSummary.observe(this, androidx.lifecycle.Observer {
            it?.let {
                binding.searchbarTextView.text = it.title
                binding.searchbarTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray1))
            }
        })

        return root
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
                0 -> tab.text = "홈"
                1 -> tab.text = "메뉴"
                2 -> tab.text = "리뷰"
            }
        }.attach()


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

        naver_map!!.setOnMapClickListener { pointF, latLng ->
            if(viewmodel.isShowBottomSheetTab.value == true) { //다른 마커 클릭

            } else {
                val bottomSheet = binding.bottomSheetLayout
                val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
                val density = resources.displayMetrics.density
                persistenetBottomSheet.peekHeight = (150 * density).toInt()
                persistenetBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

                viewmodel._selectedMarker.value = null
                viewmodel._BottomSheetStep1.value = true
                viewmodel._BottomSheetState.value = 0

            }

        }


        // 위치 추적 기능 사용 여부 확인 (퍼미션 체크 이루어짐)
        locationSource =
            FusedLocationSource(this, getString(R.string.FUSED_LOCATION_CODE).toInt())

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

            val bottomSheet = binding.bottomSheetLayout
            val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
            val density = resources.displayMetrics.density
            persistenetBottomSheet.peekHeight = (150 * density).toInt()
            viewmodel._BottomSheetState.value = 0
            persistenetBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

            startActivityForResult(searchIntent, getString(R.string.SEARCH_RESULT_OK).toInt())
        }


    }



    // GPS가 켜져있는지 확인
    private fun checkOnOffGPS(): Boolean {
        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // gps 켜져 있음
            // 위치 추적을 위한 위치 정보 권한 확인
            requestPermissions(permission_list, getString(R.string.PERMISSIONS_REQUEST_CODE).toInt())
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
                startActivityForResult(callGPSSettingIntent, getString(R.string.GPS_ENABLE_REQUEST_CODE).toInt())
                // GPS 설정하러 설정 화면 넘어감
            }
            .setNegativeButton("취소") { dialog, id ->
                //
                dialog.cancel()
            }
            .show()
    }

    fun setLocation(x : Double, y:Double) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(y, x))
        naver_map!!.moveCamera(cameraUpdate)
    }

    // GPS 권한 설정 콜백
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 요청 코드가 일치하고 결과 코드가 성공인 경우
        if (requestCode == getString(R.string.SEARCH_RESULT_OK).toInt() && resultCode == Activity.RESULT_OK) {
            val resultData = data?.getParcelableExtra<SearchedRestaurantItem>("search_item")
            // 결과 데이터 처리
            Log.d("MyFragment", "Received result data: $resultData")
        }

        when (requestCode) {
            getString(R.string.GPS_ENABLE_REQUEST_CODE).toInt() -> {
                //사용자가 GPS 활성 시켰는지 검사
                requestPermissions(permission_list, getString(R.string.PERMISSIONS_REQUEST_CODE).toInt())
            }
            getString(R.string.SEARCH_RESULT_OK).toInt() -> {
                if(data != null){
                    val serahed_item = data.getParcelableExtra<SearchedRestaurantItem>("search_item")

                    if(serahed_item!!.placeId != null){
                        // 검색한 가게가 마커
                        val markerList = viewmodel._markerList.value!!
                        viewmodel._selectedMarker.value = markerList.find { it.placeId == serahed_item.placeId }
                    }
                    // 검색한 위치로 이동
                    setLocation(serahed_item.x.toDouble(), serahed_item.y.toDouble())
                }

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
            getString(R.string.FUSED_LOCATION_CODE).toInt() -> {
                // 위치 퍼미션 허용 안 함
                if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    //퍼미션 요청
                    requestPermissions(permission_list, getString(R.string.PERMISSIONS_REQUEST_CODE).toInt())
                } else {
                    // 위치 추적 설정
                    naver_map!!.locationSource = locationSource
                    naver_map!!.locationTrackingMode = LocationTrackingMode.Follow
                }

            }
            // 위치 퍼미션 사용
            getString(R.string.PERMISSIONS_REQUEST_CODE).toInt() -> {
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


    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("프래그먼트 생명주기", "onDestroyView")
        viewmodel.removeBookmark()
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
            /*
            if(step == 2) {
                Log.d("onFling","3단계")
                val bottomSheet = binding.bottomSheetLayout
                val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)
                persistenetBottomSheet.state = STATE_EXPANDED
                step = 3
            }

             */
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
                val bottomSheet = binding.bottomSheetLayout
                val persistenetBottomSheet = BottomSheetBehavior.from(bottomSheet)

                if (distanceY < 0) {
                    if (viewmodel.BottomSheetState.value == 1) { //step == 1 persistenetBottomSheet.state == STATE_COLLAPSED
                        Log.d("onFling","2단계")
                        viewmodel._isShowBottomSheetTab.value = true

                        val density = resources.displayMetrics.density

                        //persistenetBottomSheet.state = STATE_HALF_EXPANDED

                        persistenetBottomSheet.peekHeight = (450 * density).toInt()
                        persistenetBottomSheet.state = STATE_COLLAPSED

                        /*
                        // 초기 peek height 설정
                        val initialPeekHeight = persistenetBottomSheet.peekHeight
                        // 변경하고자 하는 높이
                        val targetPeekHeight = (450 * density).toInt()

                        ValueAnimator.ofInt(initialPeekHeight, targetPeekHeight).apply {
                            duration = 100
                            interpolator = AccelerateDecelerateInterpolator()

                            addUpdateListener { animator ->
                                val animatedValue = animator.animatedValue as Int
                                persistenetBottomSheet.setPeekHeight(animatedValue)
                                bottomSheet.requestLayout()
                            }
                            start()
                        }

                         */
                        viewmodel._BottomSheetStep1.value = false
                        viewmodel._BottomSheetState.value = 2
                    } else if (viewmodel._BottomSheetState.value == 2) {
                        persistenetBottomSheet.state = STATE_EXPANDED

                        viewmodel._BottomSheetState.value = 3
                        // 새로운 fragment 호출

                    }
                } else if(distanceY > 0) {
                    if (viewmodel.BottomSheetState.value == 2) { //step == 1 persistenetBottomSheet.state == STATE_HALF_EXPANDED
                        Log.d("onFling", "2단계")
                        viewmodel._isShowBottomSheetTab.value = true
                        val density = resources.displayMetrics.density
                        persistenetBottomSheet.peekHeight = (150 * density).toInt()
                        persistenetBottomSheet.state = STATE_COLLAPSED

                        /*
                        val initialPeekHeight = persistenetBottomSheet.peekHeight
                        // 변경하고자 하는 높이
                        val targetPeekHeight = (150 * density).toInt()

                        ValueAnimator.ofInt(initialPeekHeight, targetPeekHeight).apply {
                            duration = 100
                            interpolator = AccelerateDecelerateInterpolator()

                            addUpdateListener { animator ->
                                val animatedValue = animator.animatedValue as Int
                                persistenetBottomSheet.setPeekHeight(animatedValue)
                                bottomSheet.requestLayout()
                            }

                            start()
                        }

                         */
                        /*
                        persistenetBottomSheet.peekHeight = (150 * density).toInt()
                        persistenetBottomSheet.state = STATE_COLLAPSED

                         */
                        viewmodel._BottomSheetStep1.value = true
                        viewmodel._BottomSheetState.value = 1
                    }

                }
                return true
            }
            return false
        }
    }



}

