package com.android.aviro.presentation.home.ui.map

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantSummary
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

    var _markerList = MutableLiveData<List<MarkerEntity>?>() // 화면에 새로 그릴 마커 리스트
    val markerList : LiveData<List<MarkerEntity>?>
        get() = _markerList

    /*
    var _updatedMarkerList = MutableLiveData<List<MarkerEntity>>()
    val updatedMarkerList : LiveData<List<MarkerEntity>>
        get() = _updatedMarkerList

     */

    var _bookmarkList = MutableLiveData<List<String>>()
    val bookmarkList : LiveData<List<String>>
        get() = _bookmarkList

    var _isAppRunning = MutableLiveData<Boolean>()
    val isAppRunning : LiveData<Boolean>
        get() = _isAppRunning

    var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    var _selectedMarker = MutableLiveData<MarkerEntity?>()
    val selectedMarker : LiveData<MarkerEntity?>
        get() = _selectedMarker

    var _selectedItemSummary = MutableLiveData<RestaurantSummary?>()
    val selectedItemSummary : LiveData<RestaurantSummary?>
        get() = _selectedItemSummary

    var _isShowBottomSheetTab = MutableLiveData<Boolean>()
    val isShowBottomSheetTab : LiveData<Boolean>
        get() = _isShowBottomSheetTab

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
        }
    }

    fun setNaverMap(naver_map : NaverMap) {
        _naverMap.postValue(naver_map)
    }

    // 맵 객체가 새로 생길때 호출 됩니다
    fun updateMap(naver_map : NaverMap, initMap : Boolean) {
        Log.d("Map","맵 초기화")
        setNaverMap(naver_map)
        viewModelScope.launch {
                // 모드 마커 데이터 다 가져와야 함
                getRestaurantUseCase("0.0", "0.0", "100", "", initMap).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            val data = it.data as ReataurantReponseDTO
                            //Log.d("Map","${data}")
                            val new_marker_list =
                                getRestaurantUseCase.getMarker(initMap, data) // 동기 처리
                            //_markerList.value.a
                            if (new_marker_list != null) {
                                // 즐겨찾기 누르고 있는 상태인 경우
                                // 즐겨찾기 누르고 있지 않은 경우
                                drawBasicMarker(naver_map, new_marker_list)
                            }

                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message

                        }
                    }
                }
            }
        }

    // 기본 마커 그림
    fun drawBasicMarker(naver_map : NaverMap, newMarker : List<MarkerEntity>) {
        newMarker.map { markerEntity ->
            markerEntity.marker.map = naver_map

            // 클릭했을 때
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
        }
    }

    fun getBookmark() {
        // 즐찾 리스트를 가져옴
        viewModelScope.launch {
            getBookmarkRestaurantUseCase().onSuccess {
                val code = it.statusCode
                if(code == 200) {
                    //_bookmarkList.postValue(it.bookmarks)
                    _bookmarkList.value = it.bookmarks
                    Log.d("bookmark","${it.bookmarks}")

                    // 모든 리스트 null 처리
                    val markerList = getRestaurantUseCase.getMarker()

                    // 즐찾 마커만 반환
                    //val markerList = getRestaurantUseCase.getMarker(bookmarkList.value!!)

                    // 이미지 변환  // 클릭 메서드 다시 설정
                    markerList!!.map { markerEntity ->
                        if (bookmarkList.value?.contains(markerEntity.placeId) == false) {
                            markerEntity.marker.map = null
                        } else {
                            when (markerEntity.veganType) {
                                "green" -> {
                                    markerEntity.marker.icon =
                                        OverlayImage.fromResource(R.drawable.marker_bookmark_green)
                                }
                                "orange" -> {
                                    markerEntity.marker.icon =
                                        OverlayImage.fromResource(R.drawable.marker_bookmark_orange)
                                }
                                "yellow" -> {
                                    markerEntity.marker.icon =
                                        OverlayImage.fromResource(R.drawable.marker_bookmark_yellow)
                                }
                            }
                            markerEntity.marker.setOnClickListener {
                                when(markerEntity.veganType) {
                                    "green" -> {
                                        markerEntity.marker.icon =
                                            OverlayImage.fromResource(R.drawable.marker_bookmark_select_yellow)
                                    }
                                    "orange" -> {
                                            markerEntity.marker.icon =
                                                OverlayImage.fromResource(R.drawable.marker_bookmark_select_orange)
                                    }
                                    "yellow" -> {
                                            markerEntity.marker.icon =
                                                OverlayImage.fromResource(R.drawable.marker_bookmark_select_yellow)
                                    }
                                }
                                // 선택한 마커
                                _selectedMarker.value = markerEntity

                                false
                            }

                        }
                    }


                }
            }

        }

    }

    fun removeBookmark() {
        // 즐겨찾기 마커 삭제
        // 맵 객체 삭제
        // 마커 이미지 다시 원위치
        val markerList = getRestaurantUseCase.getMarker()
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
        Log.d("selectedMarker","${selectedMarker}")
        if(_selectedMarker.value != null) {
            viewModelScope.launch {
                getRestaurantDetailUseCase.getSummary(_selectedMarker.value!!.placeId).let {
                    when(it) {
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