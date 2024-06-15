package com.aviro.android.presentation.home.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.common.getBookmarkMarkerIcon
import com.aviro.android.common.getMarkerIcon
import com.aviro.android.common.getSelectedMarkerIcon
import com.aviro.android.common.getVeganType
import com.aviro.android.common.setBookmarkMarkerClickListener
import com.aviro.android.common.setMarkerClickListener
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.challenge.ChallengePopUp
import com.aviro.android.domain.entity.marker.MarkerOfMap
import com.aviro.android.domain.entity.restaurant.*
import com.aviro.android.domain.usecase.challenge.GetChallengeInfo
import com.aviro.android.domain.usecase.member.UpdateBookmarkUseCase
import com.aviro.android.domain.usecase.retaurant.GetBookmarkRestaurantUseCase
import com.aviro.android.domain.usecase.retaurant.GetRestaurantDetailUseCase
import com.aviro.android.domain.usecase.retaurant.GetRestaurantUseCase
import com.aviro.android.domain.usecase.retaurant.ReportRestaurantUseCase
import com.naver.maps.map.NaverMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (
    private val getRestaurantUseCase : GetRestaurantUseCase,
    private val getBookmarkRestaurantUseCase : GetBookmarkRestaurantUseCase,
    private val getRestaurantDetailUseCase : GetRestaurantDetailUseCase,
    private val reportRestaurantUseCase : ReportRestaurantUseCase,
    private val updateBookmarkUseCase : UpdateBookmarkUseCase,
    private val getChallengeInfo : GetChallengeInfo,
) : ViewModel() {

    var _isFirst = MutableLiveData<Boolean>() // 첫 맵화면
    val isFirst : LiveData<Boolean>
        get() = _isFirst

    var _isChange = MutableLiveData<Boolean>() // 식당 변경 사항 있음
    val isChange : LiveData<Boolean>
        get() = _isChange

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

    // 선택한 마커
    var _selectedMarker = MutableLiveData<MarkerOfMap?>()
    val selectedMarker : LiveData<MarkerOfMap?>
        get() = _selectedMarker

    var _restaurantSummary = MutableLiveData<RestaurantSummary>()
    val restaurantSummary : LiveData<RestaurantSummary>
        get() = _restaurantSummary

    var _promotionData = MutableLiveData<List<String>>()
    val promotionData : LiveData<List<String>>
        get() = _promotionData


    // 바텀시트 보여지는지 여부
    var _isShowBottomSheetTab = MutableLiveData<Boolean>()
    val isShowBottomSheetTab : LiveData<Boolean>
        get() = _isShowBottomSheetTab

    var _bottomSheetState = MutableLiveData<Int>()
    val bottomSheetState : LiveData<Int>
        get() = _bottomSheetState

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    private val _diallogLiveData = MutableLiveData<String?>()
    val diallogLiveData: LiveData<String?> get() = _diallogLiveData

    private val _naverMap = MutableLiveData<NaverMap>()
    val naverMap: LiveData<NaverMap> get() = _naverMap

    private val _reportCode = MutableLiveData<Int>()
    val reportCode: LiveData<Int> get() = _reportCode

    private val _categoryFilter = MutableLiveData<List<Boolean>>()
    val categoryFilter: LiveData<List<Boolean>> get() = _categoryFilter


    init {

        _isAppRunning.value = false
        _isFavorite.value = false
        _isFirst.value = true
        _isShowBottomSheetTab.value = false
        _selectedMarker.value = null

        _categoryFilter.value = listOf(false, false, false, false)

    }

    fun getCategoryFilterBool(category : String) : Boolean? {
        if(category == "식당") {
            return _categoryFilter.value!![0]
        }
        else if(category == "카페") {
            return _categoryFilter.value!![1]
        }
        else if(category == "빵집") {
            return _categoryFilter.value!![2]
        }
        else if(category == "술집") {
            return _categoryFilter.value!![3]
        }
        return null
    }

    fun updateCategoryFilter(idx : Int, value : Boolean) {
        _categoryFilter.value?.let {
            val updatedList = it.toMutableList()
            updatedList[idx] = value
            _categoryFilter.value = updatedList
        }
    }


     fun getPopInfo() {
        viewModelScope.launch {
            getChallengeInfo.getChallengeImg().let {
                when(it){
                    is MappingResult.Success<*> -> {
                        if (it.data != null) {
                            val data = it.data as ChallengePopUp
                            _promotionData.value = listOf(data.imageUrl)

                        }
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
            }
            }
        }
    }



    // 맵 객체가 새로 생길때 호출 됩니다
    fun updateMap(naver_map : NaverMap, initMap : Boolean) {
        _naverMap.value = naver_map

        viewModelScope.launch {
                // 모드 마커 데이터 다 가져와야 함
                getRestaurantUseCase(initMap, "0.0", "0.0", "0.0", "").let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            if (it.data != null) {
                                val data = it.data as List<MarkerOfMap>

                                if(data.isNotEmpty()){
                                    if(_markerList.value == null) {
                                        Log.d("updateMap","초기화")
                                        _markerList.value = data
                                        drawBasicMarker(naver_map, data)
                                    } else {
                                        data.map { new ->
                                            if (markerList.value!!.filter { it.placeId == new.placeId }
                                                    .isEmpty()) {
                                                Log.d("updateMap", "추가:${new}")
                                                _markerList.value = _markerList.value?.plus(data)
                                                drawBasicMarker(naver_map, data)
                                            } else {
                                                Log.d("updateMap", "업데이트:${new}")
                                                drawUpdatedMarker(new)

                                                // 한개 아니라 여러개면...?
                                                //updateVeganType(new.placeId, new.category, new.allVegan, new.someMenuVegan, new.ifRequestVegan)
                                            }
                                        }
                                    }

                                }
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

    fun updateVeganType(placeId : String, allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean) { // category : String,
        // 마커 리스트의 내용 바꿔주기
        val marker = _markerList.value?.filter { it.placeId == placeId }?.first()

        marker?.let {  markerOfMap ->
            markerOfMap.veganTypeColor = getVeganType(allVegan, someMenuVegan, ifRequestVegan)
            //markerOfMap.category = category
            markerOfMap.marker.icon = getSelectedMarkerIcon(markerOfMap.category, allVegan, someMenuVegan, ifRequestVegan )
            markerOfMap.allVegan = allVegan
            markerOfMap.someMenuVegan = someMenuVegan
            markerOfMap.ifRequestVegan = ifRequestVegan

            //_selectedMarker.value = markerOfMap
            markerOfMap.marker.setOnClickListener {
                cancelSelectedMarker(_selectedMarker.value) // 먼저 클릭된 마커 있는 경우 변경
                // 클릭했을 때 나올 마커 셋팅
                markerOfMap.marker.icon = setMarkerClickListener(markerOfMap.category, markerOfMap.veganTypeColor)
                // 선택한 마커
                _selectedMarker.value = markerOfMap

                false
            }

            markerOfMap.marker.performClick()
            //getSelectedMarkerIcon(markerOfMap.category, allVegan, someMenuVegan, ifRequestVegan)

            Log.d("updateMap:updateVeganType","${markerOfMap}")
        }
    }

    fun updateCategory(placeId : String, category : String) {
        // 로컬에는 맵화면 다시 활성화 될때 반영
        val marker = _markerList.value?.filter { it.placeId == placeId }?.first()

        marker?.let {  markerOfMap ->

            markerOfMap.category = category
            markerOfMap.marker.icon = getSelectedMarkerIcon(category, markerOfMap.allVegan, markerOfMap.someMenuVegan, markerOfMap.ifRequestVegan )//getSelectedMarkerIcon(markerOfMap.category, allVegan, someMenuVegan, ifRequestVegan)

            //_selectedMarker.value = markerOfMap

            markerOfMap.marker.setOnClickListener {
                cancelSelectedMarker(_selectedMarker.value) // 먼저 클릭된 마커 있는 경우 변경
                // 클릭했을 때 나올 마커 셋팅
                markerOfMap.marker.icon = setMarkerClickListener(category, markerOfMap.veganTypeColor)
                // 선택한 마커
                _selectedMarker.value = markerOfMap
                false
            }
            Log.d("updateMap:updateCategory","${markerOfMap}")

            markerOfMap.marker.performClick()

        }
    }

    // 기본 마커 그림
    fun drawBasicMarker(naver_map : NaverMap, newMarker : List<MarkerOfMap>) {

        newMarker.map { markerOfMap ->
            markerOfMap.marker.map = naver_map

           // 마커 클릭 메서드
            markerOfMap.marker.setOnClickListener {
                cancelSelectedMarker(_selectedMarker.value) // 먼저 클릭된 마커 있는 경우 변경
                // 클릭했을 때 나올 마커 셋팅
                markerOfMap.marker.icon = setMarkerClickListener(markerOfMap.category, markerOfMap.veganTypeColor)
                // 선택한 마커
                _selectedMarker.value = markerOfMap

                false
            }
        }
    }

    fun drawUpdatedMarker(updatedMarker : MarkerOfMap) {
        val marker = _markerList.value?.filter { it.placeId == updatedMarker.placeId }?.first()

        marker?.let {  markerOfMap ->

            markerOfMap.veganTypeColor = getVeganType(updatedMarker.allVegan, updatedMarker.someMenuVegan, updatedMarker.ifRequestVegan)
            markerOfMap.category = updatedMarker.category
            markerOfMap.marker.icon = getMarkerIcon(markerOfMap.category, updatedMarker.allVegan, updatedMarker.someMenuVegan, updatedMarker.ifRequestVegan )
            markerOfMap.allVegan = updatedMarker.allVegan
            markerOfMap.someMenuVegan = updatedMarker.someMenuVegan
            markerOfMap.ifRequestVegan = updatedMarker.ifRequestVegan

            //_selectedMarker.value = markerOfMap

            markerOfMap.marker.setOnClickListener {
                cancelSelectedMarker(_selectedMarker.value) // 먼저 클릭된 마커 있는 경우 변경
                // 클릭했을 때 나올 마커 셋팅
                markerOfMap.marker.icon = setMarkerClickListener(updatedMarker.category, markerOfMap.veganTypeColor)
                // 선택한 마커
                _selectedMarker.value = markerOfMap
                false
            }
            _selectedMarker.value?.let {
                // 업데이트 된 마커가 클릭된 아이인 경우
                if(it.placeId == updatedMarker.placeId) {
                    //markerOfMap.marker.icon = getSelectedMarkerIcon(updatedMarker.category, markerOfMap.allVegan, markerOfMap.someMenuVegan, markerOfMap.ifRequestVegan )//getSelectedMarkerIcon(markerOfMap.category, allVegan, someMenuVegan, ifRequestVegan)
                    markerOfMap.marker.performClick()
                }
            }

        }
    }

    fun getRestaurantSummary(placeId : String) {
        viewModelScope.launch {
            getRestaurantDetailUseCase.getSummary(placeId).let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        _restaurantSummary.value = it.data as RestaurantSummary

                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                    else -> {}
                }
            }
        }
    }


    fun getBookmarkList() {
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

                                markerOfMap.marker.icon = getBookmarkMarkerIcon(markerOfMap.allVegan, markerOfMap.someMenuVegan, markerOfMap.ifRequestVegan)
                                markerOfMap.marker.setOnClickListener {
                                    // 이전 마커 변경
                                    cancelSelectedBookmarkMarker(_selectedMarker.value)
                                    markerOfMap.marker.icon = setBookmarkMarkerClickListener(markerOfMap.veganTypeColor)
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

    fun selectCategory() {

    }

    fun cancelSelectedMarker(pre_selected_msrekr : MarkerOfMap?) {
        Log.d("cancelSelectedMarker", "$pre_selected_msrekr")
        pre_selected_msrekr?.let{
            pre_selected_msrekr.marker.icon = getMarkerIcon(pre_selected_msrekr.category, pre_selected_msrekr.allVegan, pre_selected_msrekr.someMenuVegan, pre_selected_msrekr.ifRequestVegan)
        }
    }

    fun cancelSelectedBookmarkMarker(pre_selected_msrekr : MarkerOfMap?) {

        pre_selected_msrekr?.let{
            pre_selected_msrekr.marker.icon = getBookmarkMarkerIcon( pre_selected_msrekr.allVegan, pre_selected_msrekr.someMenuVegan, pre_selected_msrekr.ifRequestVegan)
        }
    }

    fun removeBookmarkList() {
        // 즐겨찾기 마커 삭제
        // 맵 객체 삭제
        // 마커 이미지 다시 원위치
        val markerList = _markerList.value
        markerList!!.map { markerEntity ->

            if (bookmarkList.value?.contains(markerEntity.placeId) == true) {
                markerEntity.marker.icon = getMarkerIcon(markerEntity.category, markerEntity.allVegan, markerEntity.someMenuVegan, markerEntity.ifRequestVegan)
                _selectedMarker.value = null
                // 북마크 마커 중 선택이 되어 있던 아이
                /*
                if(selectedMarker.value != null && markerEntity.placeId == selectedMarker.value!!.placeId) {
                        markerEntity.marker.icon = setBookmarkMarkerClickListener(selectedMarker.value!!.veganTypeColor)

                } else {
                    markerEntity.marker.icon = getMarkerIcon(markerEntity.category, markerEntity.allVegan, markerEntity.someMenuVegan, markerEntity.ifRequestVegan)
                }

                 */

                markerEntity.marker.setOnClickListener {
                    markerEntity.marker.icon = setMarkerClickListener(markerEntity.category, markerEntity.veganTypeColor)
                    // 이전 마커 변경
                    cancelSelectedMarker(_selectedMarker.value)
                    // 선택한 마커
                    _selectedMarker.value = markerEntity
                    false
                }


            } else {
                markerEntity.marker.map = naverMap.value // 북마크 아니었던 마커 지도에 표시
            }
        }
    }


    fun onClickFavorite() {
        // 플로팅 아이콘 설정
        _isFavorite.value = !isFavorite.value!!  // != true

        Log.d("isFavorite","${_isFavorite.value}")
        // 즐겨찾기 설정한 가게만 보이도록
        if(_isFavorite.value == true) {
            getBookmarkList()
        } else {
            removeBookmarkList()
        }
    }




    fun setReportCode(code : Int) {
        Log.d("setReportCode","가게 신고 코드")
        _reportCode.value = code
        reportRestaurant()
    }

    fun reportRestaurant() {
        Log.d("reportRestaurant","가게 신고 완료")
        viewModelScope.launch {
            reportRestaurantUseCase.invoke(selectedMarker.value!!.placeId, reportCode.value!!).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        _diallogLiveData.value = it.message
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }

            }
        }

    }


}