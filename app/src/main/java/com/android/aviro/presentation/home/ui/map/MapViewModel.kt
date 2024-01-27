package com.android.aviro.presentation.home.ui.map

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.BuildConfig
import com.android.aviro.R
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.data.entity.restaurant.LocOfRestaurant
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (
    private val getRestaurantUseCase : GetRestaurantUseCase
) : ViewModel() {

    var _isFirst = MutableLiveData<Boolean>()
    val isFirst : LiveData<Boolean>
        get() = _isFirst

    var _isChange = MutableLiveData<Boolean>()
    val isChange : LiveData<Boolean>
        get() = _isChange

    var _markerList = MutableLiveData<List<MarkerEntity>?>()
    val markerList : LiveData<List<MarkerEntity>?>
        get() = _markerList

    var _updatedMarkerList = MutableLiveData<List<MarkerEntity>>()
    val updatedMarkerList : LiveData<List<MarkerEntity>>
        get() = _updatedMarkerList

    var _isAppRunning = MutableLiveData<Boolean>()
    val isAppRunning : LiveData<Boolean>
        get() = _isAppRunning

    var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    var _selectedMarker = MutableLiveData<String>()
    val selectedMarker : LiveData<String>
        get() = _selectedMarker

    var _isShowBottomSheetTab = MutableLiveData<Boolean>()
    val isShowBottomSheetTab : LiveData<Boolean>
        get() = _isShowBottomSheetTab

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData


    init {
        Log.d("ViewModel","Map ViewModel 생성 초기화")
        // 비동기 처리
        viewModelScope.launch {
            _isAppRunning.value = false
            _isFavorite.value = false
            _isFirst.value = true
            _isShowBottomSheetTab.value = false
        }
    }

    // 맵 객체가 새로 생길때 호출 됩니다
    fun updateMap(naver_map : NaverMap, initMap : Boolean) {
        Log.d("Map","맵 초기화")
        viewModelScope.launch {
                // 모드 마커 데이터 다 가져와야 함
                getRestaurantUseCase("0.0", "0.0", "100", "", initMap).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            val data = it.data as ReataurantReponseDTO
                            //Log.d("Map","${data}")
                            val new_marker_list =
                                getRestaurantUseCase.getMarker(initMap, data) // 동기 처리
                            if (new_marker_list != null) drawMarker(naver_map, new_marker_list)

                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message

                        }
                    }
                }
            }
        }

    fun drawMarker(naver_map : NaverMap,newMarker : List<MarkerEntity>) {
        newMarker.map { markerEntity ->
            markerEntity.marker.map = naver_map

            markerEntity.marker.setOnClickListener {
                when (markerEntity.veganType) {
                    "green" -> markerEntity.marker.icon =
                        OverlayImage.fromResource(R.drawable.marker_select_green)
                    "orange" -> markerEntity.marker.icon =
                        OverlayImage.fromResource(R.drawable.marker_select_orange)
                    "yellow" -> markerEntity.marker.icon =
                        OverlayImage.fromResource(R.drawable.marker_select_yellow)
                }
                // 선택한 마커
                _selectedMarker.value = markerEntity.placeId

                false
            }
        }
    }

    fun onClickFavorite(view : View) {
        // 플로팅 아이콘 설정
        _isFavorite.value = isFavorite.value != true

        // 즐겨찾기 설정한 가게만 보이도록

    }

}