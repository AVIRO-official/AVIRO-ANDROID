package com.aviro.android.presentation.bottomsheet

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.marker.MarkerOfMap
import com.aviro.android.domain.entity.restaurant.*
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.domain.usecase.challenge.GetChallengeInfo
import com.aviro.android.domain.usecase.member.DeleteMyReviewUseCase
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import com.aviro.android.domain.usecase.member.UpdateBookmarkUseCase
import com.aviro.android.domain.usecase.retaurant.GetRestaurantDetailUseCase
import com.aviro.android.domain.usecase.retaurant.PostRestaurantRecommendUseCase
import com.aviro.android.domain.usecase.retaurant.ReportReviewUseCase
import com.aviro.android.presentation.entity.RestaurantInfoForUpdateEntity
import com.aviro.android.presentation.entity.ReviewInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    private val getRestaurantDetailUseCase : GetRestaurantDetailUseCase,
    private val updateBookmarkUseCase : UpdateBookmarkUseCase,
    private val getChallengeInfo : GetChallengeInfo,
    private val postRestaurantRecommendUseCase : PostRestaurantRecommendUseCase,
    private val deleteMyReviewUseCase : DeleteMyReviewUseCase,
    private val reportReviewUseCase : ReportReviewUseCase
) : ViewModel() {

    var userNickname : String? = null

 /*   var _isFavoriteOfSelectedMarker = MutableLiveData<Boolean>()
    val isFavoriteOfSelectedMarker : LiveData<Boolean>
        get() =_isFavoriteOfSelectedMarker*/

    var _selectedMarker = MutableLiveData<MarkerOfMap>()
    val selectedMarker : LiveData<MarkerOfMap>
        get() = _selectedMarker

    var _restaurantSummary = MutableLiveData<RestaurantSummary>()
    val restaurantSummary : LiveData<RestaurantSummary>
        get() = _restaurantSummary

    var _restaurantInfo = MutableLiveData<RestaurantInfo>()
    val restaurantInfo : LiveData<RestaurantInfo>
        get() = _restaurantInfo

    var _isLike = MutableLiveData<Boolean>()
    val isLike : LiveData<Boolean>
        get() = _isLike

    var _restaurantTimetable = MutableLiveData<RestaurantTimetable>()
    val restaurantTimetable : LiveData<RestaurantTimetable>
        get() = _restaurantTimetable

    var _menuList = MutableLiveData<RestaurantMenu>()
    val menuList : LiveData<RestaurantMenu>
        get() = _menuList

    var _reviewList = MutableLiveData<RestaurantReview?>()
    val reviewList : LiveData<RestaurantReview?>
        get() = _reviewList


    // 가게명, 위치, 카테고리, 메뉴 리스트 -> 업데이트시 필요
    var _restaurantDataForUpdate = MutableLiveData<RestaurantInfoForUpdateEntity>()
    val restaurantDataForUpdate : LiveData<RestaurantInfoForUpdateEntity>
        get() =_restaurantDataForUpdate

    var _bottomSheetSate = MutableLiveData<Int>()
    val bottomSheetSate : LiveData<Int>
        get() = _bottomSheetSate

    private val _selectedReviewForUpdate = MutableLiveData<ReviewInfo>()
    val selectedReviewForUpdate: LiveData<ReviewInfo>
        get() = _selectedReviewForUpdate

    val _selectedReviewForReport = MutableLiveData<Review>()
    val selectedReviewForReport: LiveData<Review>
        get() = _selectedReviewForReport

    var _reviewReportNumber = MutableLiveData<Int>()
    val reviewReportNumber : LiveData<Int>
        get() = _reviewReportNumber


    var _reviewReportContent = MutableLiveData<String?>()
    val reviewReportContent : LiveData<String?>
        get() = _reviewReportContent

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    private val _toastLiveData = MutableLiveData<String>()
    val toastLiveData: LiveData<String> get() = _toastLiveData


    init {
        getNickname()
    }


    fun getNickname() {
        viewModelScope.launch {
            getMyInfoUseCase.getNickname().let {
                Log.d("getMyInfoUseCase", "${it}")
                when(it) {
                    is MappingResult.Success<*> -> { userNickname = it.data.toString() }
                    is MappingResult.Error -> _errorLiveData.value = it.message
                }
            }
        }
    }

    fun getRestaurantInfo(placeId : String) {
        Log.d("getRestaurantInfo","가게 정보 : ${placeId}")
            viewModelScope.launch {
                getRestaurantDetailUseCase.getInfo(placeId).let {
                    Log.d("getRestaurantInfo","가게 정보 : ${it}")
                    when (it) {
                        is MappingResult.Success<*> -> { _restaurantInfo.value = it.data as RestaurantInfo }
                        is MappingResult.Error -> { _errorLiveData.value = it.message }
                    }
                }
            }
    }

    fun getRestaurantMenu(placeId : String) {
            viewModelScope.launch {
                getRestaurantDetailUseCase.getMenu(placeId).let {
                    Log.d("getRestaurantMenu", "${it}")
                    when (it) {
                        is MappingResult.Success<*> -> { _menuList.value = it.data as RestaurantMenu }
                        is MappingResult.Error -> { _errorLiveData.value = it.message }
                    }
                }
            }
        }

    fun getRestaurantReview(placeId : String) {
            viewModelScope.launch {
                getRestaurantDetailUseCase.getReview(placeId).let {
                    //Log.d("getRestaurantReview", "${it}")
                    when (it) {
                        is MappingResult.Success<*> -> { _reviewList.value = it.data as RestaurantReview }
                        is MappingResult.Error -> { _errorLiveData.value = it.message }
                    }
                }
            }
    }

    fun getRestaurantTimetable(placeId : String) {
        viewModelScope.launch {
            getRestaurantDetailUseCase.getTimetable(placeId).let {
                when (it) {
                    is MappingResult.Success<*> -> { _restaurantTimetable.value = it.data as RestaurantTimetable }
                    is MappingResult.Error -> { _errorLiveData.value = it.message }
                }
            }
        }
    }



    fun setRestaurantData(markerData : MarkerOfMap) {
        _restaurantDataForUpdate.value = RestaurantInfoForUpdateEntity(markerData.placeId,
            markerData.title,markerData.category, restaurantInfo.value!!.address, restaurantInfo.value!!.address2,
            restaurantInfo.value!!.phone, restaurantInfo.value!!.url, markerData.x, markerData.y,
            markerData.allVegan, markerData.someMenuVegan, markerData.ifRequestVegan,
            menuList.value!!.menuArray)
    }


    fun updateBookmark() { //placeId : String, isLike : Boolean
        viewModelScope.launch {
            Log.d("updateBookmark","${isLike.value}")
            if(isLike.value == true){
                // 즐겨찾기 해제
                updateBookmarkUseCase.deleteBookmark(selectedMarker.value!!.placeId).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            _toastLiveData.value = it.message ?: "즐겨찾기가 삭제되었습니다."
                        }

                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message ?: "즐겨찾기 삭제에 실패했습니다.\n다시 시도해주세요."
                        }
                    }
                }
            } else {
                updateBookmarkUseCase.addBookmark(selectedMarker.value!!.placeId).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            _toastLiveData.value = it.message ?: "즐겨찾기가 추가되었습니다."
                        }

                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message ?: "즐겨찾기 추가에 실패했습니다.\n다시 시도해주세요."
                        }
                    }
                }
            }
            _isLike.value = !(isLike.value == true)
        }
    }

    fun postRecommend(isRecommend : Boolean) {
        viewModelScope.launch {
            postRestaurantRecommendUseCase(selectedMarker.value!!.placeId, isRecommend)
        }
    }

    fun reportReview(code : Int, content : String?) {
        // 신고 번호
        //Log.d("bottomSheetDialog", "${_selectedMarker.value!!.title}, ${_selectedReviewForReport.value!!}, ${code},${content}")

        viewModelScope.launch {
            reportReviewUseCase(selectedMarker.value!!.title , selectedReviewForReport.value!!, code + 1, content).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        _toastLiveData.value = it.message ?: "신고가 완료되었습니다."
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message ?: "가게 신고에 실패했습니다.\n다시 시도해주세요."
                    }
                }
            }
        }
    }

    fun deleteReview(commentId : String) {
        viewModelScope.launch {
            deleteMyReviewUseCase(commentId).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        _toastLiveData.value = it.message ?: "후기가 삭제되었습니다."
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message ?: "후기 삭제에 실패했습니다.\n다시 시도해주세요."
                    }
                }

            }
        }
    }


    fun afterTextChanged(s : Editable) {
        val reportContent = s.toString()
    }




}