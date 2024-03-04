package com.android.aviro.presentation.home.ui.map

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.data.model.marker.MarkerDAO
import com.android.aviro.data.model.restaurant.ReataurantListReponse
import com.android.aviro.data.model.restaurant.RestaurantSummaryResponse
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.marker.MarkerOfMap
import com.android.aviro.domain.entity.restaurant.BookMark
import com.android.aviro.domain.entity.restaurant.RestaurantSummary
import com.android.aviro.domain.usecase.retaurant.GetBookmarkRestaurantUseCase
import com.android.aviro.domain.usecase.retaurant.GetRestaurantDetailUseCase
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (
    private val getRestaurantUseCase : GetRestaurantUseCase,
    private val getBookmarkRestaurantUseCase : GetBookmarkRestaurantUseCase,
    private val getRestaurantDetailUseCase : GetRestaurantDetailUseCase
) : ViewModel() {

    var _isFirst = MutableLiveData<Boolean>() // 첫 맵화면
    val isFirst : LiveData<Boolean>
        get() = _isFirst

    var _isChange = MutableLiveData<Boolean>() // 식당 변경 사항 있음
    val isChange : LiveData<Boolean>
        get() = _isChange


    /*
    var _updatedMarkerList = MutableLiveData<List<MarkerEntity>>()
    val updatedMarkerList : LiveData<List<MarkerEntity>>
        get() = _updatedMarkerList

     */
    var _markerList = MutableLiveData<List<MarkerOfMap>?>() // 화면에 새로 그릴 마커 리스트
    val markerList : LiveData<List<MarkerOfMap>?>
        get() = _markerList

    var _bookmarkList = MutableLiveData<List<String>?>()
    val bookmarkList : LiveData<List<String>?>
        get() = _bookmarkList

    var _isAppRunning = MutableLiveData<Boolean>()
    val isAppRunning : LiveData<Boolean>
        get() = _isAppRunning

    var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    var _selectedMarker = MutableLiveData<MarkerOfMap?>()
    val selectedMarker : LiveData<MarkerOfMap?>
        get() = _selectedMarker

    var _selectedItemSummary = MutableLiveData<RestaurantSummary?>()
    val selectedItemSummary : LiveData<RestaurantSummary?>
        get() = _selectedItemSummary

    var _isShowBottomSheetTab = MutableLiveData<Boolean>()
    val isShowBottomSheetTab : LiveData<Boolean>
        get() = _isShowBottomSheetTab

    var _BottomSheetStep1 = MutableLiveData<Boolean>()
    val BottomSheetStep1 : LiveData<Boolean>
        get() = _BottomSheetStep1

    var _BottomSheetStep3 = MutableLiveData<Boolean>()
    val BottomSheetStep3 : LiveData<Boolean>
        get() = _BottomSheetStep3

    var _BottomSheetState = MutableLiveData<Int>()
    val BottomSheetState : LiveData<Int>
        get() = _BottomSheetState

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    private val _naverMap = MutableLiveData<NaverMap>()
    val naverMap: LiveData<NaverMap> get() = _naverMap


    init {
        Log.d("ViewModel","Map ViewModel 생성 초기화")
        // 비동기 처리
        viewModelScope.launch {
            _isAppRunning.value = false
            _isFavorite.value = false
            _isFirst.value = true
            _isShowBottomSheetTab.value = false
            _selectedMarker.value = null
            _selectedItemSummary.value = null
            _BottomSheetStep1.value = true
            _BottomSheetStep3.value = false
            _BottomSheetState.value = 0
        }
    }



    // 맵 객체가 새로 생길때 호출 됩니다
    fun updateMap(naver_map : NaverMap, initMap : Boolean) {
        _naverMap.value = naver_map
        viewModelScope.launch {
                // 모드 마커 데이터 다 가져와야 함
                getRestaurantUseCase(initMap, "0.0", "0.0", "100", "").let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            if (it.data != null) {
                                // 즐겨찾기 누르고 있는 상태인 경우
                                // 즐겨찾기 누르고 있지 않은 경우
                                //val data = it.data as List<MarkerOfMap>
                                _markerList.value = it.data as List<MarkerOfMap>
                                drawBasicMarker(naver_map, _markerList.value!!)
                            }
                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message
                        }
                        else -> {}
                    }
                }
            }
        }

    // 기본 마커 그림
    fun drawBasicMarker(naver_map : NaverMap, newMarker : List<MarkerOfMap>) {
        newMarker.map { markerOfMap ->
            markerOfMap.marker.map = naver_map

            // 클릭했을 때
            markerOfMap.marker.setOnClickListener {
                when (markerOfMap.veganType) {
                    "green" -> markerOfMap.marker.icon =
                        OverlayImage.fromResource(R.drawable.marker_default_select_green)
                    "orange" -> markerOfMap.marker.icon =
                        OverlayImage.fromResource(R.drawable.marker_default_select_orange)
                    "yellow" -> markerOfMap.marker.icon =
                        OverlayImage.fromResource(R.drawable.marker_default_select_yellow)
                }
                // 선택한 마커
                _selectedMarker.value = markerOfMap

                false
            }
        }
    }

    // 현재 선택되어 있는 마커를 원상태로 돌림



    fun getBookmark() {
        // 즐찾 리스트를 가져옴
        viewModelScope.launch {
            getBookmarkRestaurantUseCase().let {
                when(it){
                    is MappingResult.Success<*> -> {
                        if(it.data == null) {
                            _bookmarkList.value = null
                        } else {
                            Log.d("BookMark","${it.data}")
                            //val data = it.data as BookMark
                            val data = it.data.let { BookMark(it as List<String>)}
                            _bookmarkList.value = data.bookmarks
                        }

                        val markerList = _markerList.value
                        // 북마크 아닌 경우 null처리, 이미지 변환
                        markerList?.map { markerOfMap ->
                            if (bookmarkList.value?.contains(markerOfMap.placeId) == false || bookmarkList.value?.contains(markerOfMap.placeId) == null) {
                                markerOfMap.marker.map = null
                            } else {
                                when (markerOfMap.veganType) {
                                    "green" -> {
                                        markerOfMap.marker.icon =
                                            OverlayImage.fromResource(R.drawable.marker_bookmark_green)
                                    }
                                    "orange" -> {
                                        markerOfMap.marker.icon =
                                            OverlayImage.fromResource(R.drawable.marker_bookmark_orange)
                                    }
                                    "yellow" -> {
                                        markerOfMap.marker.icon =
                                            OverlayImage.fromResource(R.drawable.marker_bookmark_yellow)
                                    }
                                }
                                markerOfMap.marker.setOnClickListener {
                                    when(markerOfMap.veganType) {
                                        "green" -> {
                                            markerOfMap.marker.icon =
                                                OverlayImage.fromResource(R.drawable.marker_bookmark_select_yellow)
                                        }
                                        "orange" -> {
                                            markerOfMap.marker.icon =
                                                OverlayImage.fromResource(R.drawable.marker_bookmark_select_orange)
                                        }
                                        "yellow" -> {
                                            markerOfMap.marker.icon =
                                                OverlayImage.fromResource(R.drawable.marker_bookmark_select_yellow)
                                        }
                                    }
                                    // 선택한 마커
                                    _selectedMarker.value = markerOfMap

                                    false
                                }

                            }
                        }
                    }

                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }

            }

        }

    }

    fun removeBookmark() {
        // 즐겨찾기 마커 삭제
        // 맵 객체 삭제
        // 마커 이미지 다시 원위치
        val markerList = _markerList.value
        markerList!!.map { markerEntity ->
            if (bookmarkList.value?.contains(markerEntity.placeId) == true) {
                // 마커 이미지 다시 원위치
                when (markerEntity.veganType) {
                    "green" -> {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_green)
                    }
                    "orange" -> {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_orange)
                    }
                    "yellow" -> {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_yellow)
                    }
                }
                markerEntity.marker.setOnClickListener {
                    when (markerEntity.veganType) {
                        "green" -> markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_select_green)
                        "orange" -> markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_select_orange)
                        "yellow" -> markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_select_yellow)
                    }
                    // 선택한 마커
                    _selectedMarker.value = markerEntity
                    false
                }

            } else {
                markerEntity.marker.map = naverMap.value
            }
        }
    }

    fun getRestaurantSummary() {
        if(_selectedMarker.value != null) {
            viewModelScope.launch {
                getRestaurantDetailUseCase.getSummary(_selectedMarker.value!!.placeId).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            _selectedItemSummary.value = it.data as RestaurantSummary
                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message
                        }
                        else -> {}
                    }
                }
            }
        }
    }




    fun onClickFavorite(view : View) {
        // 플로팅 아이콘 설정
        _isFavorite.value = !isFavorite.value!!  // != true

        Log.d("isFavorite","${_isFavorite.value}")
        // 즐겨찾기 설정한 가게만 보이도록
        if(_isFavorite.value == true) {
            getBookmark()
        } else {
            removeBookmark()
        }

    }


}