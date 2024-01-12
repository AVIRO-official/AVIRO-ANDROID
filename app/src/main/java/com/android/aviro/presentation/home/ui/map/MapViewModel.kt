package com.android.aviro.presentation.home.ui.map

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.data.entity.restaurant.LocOfRestaurant
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (
    private val getRestaurantUseCase : GetRestaurantUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var _markerList = MutableLiveData<MarkerListEntity>()
    val markerList : LiveData<MarkerListEntity>
        get() = _markerList


    init {
        // 비동기 처리
        viewModelScope.launch {
            // 가게 정보 불러와 DB에 저장
            //val current_time = LocalDateTime.now()
            //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd일 HH시:mm:ss")
            //val formatted = current_time.format(formatter)

        }
    }

     fun drawMarker() {
        viewModelScope.launch {
            Log.d("drawMarker","drawMarker 호출")
            getRestaurantUseCase("0.0", "0.0", "100", "")
            _markerList.value = getRestaurantUseCase.getMarker()
        }
    }

}