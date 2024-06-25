package com.aviro.android.presentation.update

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.R
import com.aviro.android.common.AmplitudeUtils
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.restaurant.BeforeAfterString
import com.aviro.android.domain.entity.restaurant.RestaurantTimetable

import com.aviro.android.domain.usecase.retaurant.UpdateRestaurantUseCase
import com.aviro.android.presentation.entity.OperatingTimeEntity
import com.aviro.android.presentation.entity.RestaurantInfoForUpdateEntity
import com.aviro.android.presentation.entity.UpdatingTimetableEntity
import com.aviro.android.presentation.mapper.toTimetableUpdating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor (
    private val updateRestaurantUseCase: UpdateRestaurantUseCase

) : ViewModel() {

    enum class UpdateType {
        LOC, TIME, NUMBER, HOMEPAGE
    }

    var _currantNavi = MutableLiveData<UpdateType>() // 마이페이지에서 어떤 목록인지
    val currantNavi : LiveData<UpdateType>
        get() = _currantNavi

    var _restaurantTimetable = MutableLiveData<RestaurantTimetable>()
    val restaurantTimetable : LiveData<RestaurantTimetable>
        get() = _restaurantTimetable


    var _restaurantInfo = MutableLiveData<RestaurantInfoForUpdateEntity>()
    val restaurantInfo : LiveData<RestaurantInfoForUpdateEntity>
        get() = _restaurantInfo

    // 변경 후 가게정보 (가게명, 위치, 카테고리, 주소, 전번, url, 메뉴, 비건타입)
    var _afterInfoData = MutableLiveData<RestaurantInfoForUpdateEntity>()
    val afterInfoData : LiveData<RestaurantInfoForUpdateEntity>
        get() = _afterInfoData

    var _categoryCheckedList = MutableLiveData<List<Boolean>>()
    val categoryCheckedList : LiveData<List<Boolean>>
        get() = _categoryCheckedList

    var _beforeCategoryData = MutableLiveData<String>()
    val beforeCategoryData : LiveData<String>
        get() = _beforeCategoryData

    var _afterCategoryData = MutableLiveData<String>()
    val afterCategoryData : LiveData<String>
        get() = _afterCategoryData

    // update시 사용할 데이터
    var _beforeTimetableData = MutableLiveData<UpdatingTimetableEntity>()
    val beforeTimetableData : LiveData<UpdatingTimetableEntity>
        get() = _beforeTimetableData

    var _afterTimetableData = MutableLiveData<UpdatingTimetableEntity>()
    val afterTimetableData : LiveData<UpdatingTimetableEntity>
        get() = _afterTimetableData

    var _afterPhoneData = MutableLiveData<String?>()
    val afterPhoneData : LiveData<String?>
        get() = _afterPhoneData

    var _afterHomepageData = MutableLiveData<String?>()
    val afterHomepageData : LiveData<String?>
        get() = _afterHomepageData

    var _SelectedOperatingData = MutableLiveData<OperatingTimeEntity>()
    val SelectedOperatingData : LiveData<OperatingTimeEntity>
        get() = _SelectedOperatingData

    var _isValidUpdate = MutableLiveData<Boolean>()
    val isValidUpdate : LiveData<Boolean>
        get() = _isValidUpdate

    var _isChangeUrl = MutableLiveData<Boolean>()
    val isChangeUrl : LiveData<Boolean>
        get() = _isChangeUrl

    var _isChangePhone = MutableLiveData<Boolean>()
    val isChangePhone : LiveData<Boolean>
        get() = _isChangePhone

    var _isChangeTime = MutableLiveData<Boolean>()
    val isChangeTime : LiveData<Boolean>
        get() = _isChangeTime

    var _isChangeRestaurantInfo = MutableLiveData<Boolean>()
    val isChangeRestaurantInfo : LiveData<Boolean>
        get() = _isChangeRestaurantInfo


    var _toast = MutableLiveData<String?>()
    val toast : LiveData<String?>
        get() = _toast

    var _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error

    companion object {
        fun getCategoryBooleanList(category : String): List<Boolean> {
            return when (category) {
                "식당" -> listOf(true, false, false, false)
                "카페" -> listOf(false, true, false, false)
                "빵집" -> listOf(false, false, true, false)
                "술집" -> listOf(false, false, false, true)
                else -> listOf(false, false, false, false)
            }
        }
    }



    fun initData() {

        _isValidUpdate.value = false

        _afterHomepageData.value = restaurantInfo.value!!.url ?: ""
        _afterPhoneData.value = restaurantInfo.value!!.phone ?: ""

        //_beforeCategoryData.value = restaurantInfo.value!!.category
        //_afterCategoryData.value = _beforeCategoryData.value
        _categoryCheckedList.value = getCategoryBooleanList(restaurantInfo.value!!.category)
        _afterInfoData.value = restaurantInfo.value!!

        _beforeTimetableData.value = UpdatingTimetableEntity(
            mon = restaurantTimetable.value?.mon?.open ?: "" ,
            monBreak = restaurantTimetable.value?.mon?.breaktime ?: "",
            tue = restaurantTimetable.value?.tue?.open ?: "",
            tueBreak = restaurantTimetable.value?.tue?.breaktime ?: "",
            wed = restaurantTimetable.value?.wed?.open ?: "",
            wedBreak = restaurantTimetable.value?.wed?.breaktime ?: "",
            thu = restaurantTimetable.value?.thu?.open ?: "",
            thuBreak = restaurantTimetable.value?.thu?.breaktime ?: "",
            fri = restaurantTimetable.value?.wed?.open ?: "",
            friBreak = restaurantTimetable.value?.wed?.breaktime ?: "",
            sat = restaurantTimetable.value?.sat?.open ?: "",
            satBreak = restaurantTimetable.value?.sat?.breaktime ?: "",
            sun = restaurantTimetable.value?.sun?.open ?: "",
            sunBreak = restaurantTimetable.value?.sun?.breaktime ?: "",
        )

        //_afterTimetableData.value = _beforeTimetableData.value!!.copy()


        _afterTimetableData.value = UpdatingTimetableEntity(
            mon = restaurantTimetable.value?.mon?.open ?: "" ,
            monBreak = restaurantTimetable.value?.mon?.breaktime ?: "",
            tue = restaurantTimetable.value?.tue?.open ?: "",
            tueBreak = restaurantTimetable.value?.tue?.breaktime ?: "",
            wed = restaurantTimetable.value?.wed?.open ?: "",
            wedBreak = restaurantTimetable.value?.wed?.breaktime ?: "",
            thu = restaurantTimetable.value?.thu?.open ?: "",
            thuBreak = restaurantTimetable.value?.thu?.breaktime ?: "",
            fri = restaurantTimetable.value?.fri?.open ?: "",
            friBreak = restaurantTimetable.value?.fri?.breaktime ?: "",
            sat = restaurantTimetable.value?.sat?.open ?: "",
            satBreak = restaurantTimetable.value?.sat?.breaktime ?: "",
            sun = restaurantTimetable.value?.sun?.open ?: "",
            sunBreak = restaurantTimetable.value?.sun?.breaktime ?: "",
        )


    }

    fun setSelectedOperatingData(data : OperatingTimeEntity) {
        _SelectedOperatingData.value = data
    }

    fun checkChangedTimetable() {
        _isChangeTime.value = (beforeTimetableData.value != afterTimetableData.value)

    }
    fun checkChangedInfo() {
        Log.d("checkChangedInfo", "${restaurantInfo.value},${afterInfoData.value}")
        _isChangeRestaurantInfo.value = (restaurantInfo.value != afterInfoData.value)

    }


    // 전화번호 변경시 감지
    fun afterTextChangedPhone(s : Editable) {
        _afterPhoneData.value = s.toString()
        _isChangePhone.value = (restaurantInfo.value!!.phone != afterPhoneData.value)
    }

    // 홈페이지 주소 변경시 감지
    fun afterTextChangedHomepage(s : Editable) {
        _afterHomepageData.value = s.toString()
        _isChangeUrl.value = (restaurantInfo.value!!.url != afterHomepageData.value)

        // 이미 변화가 있으면 굳이 안 바꿔줘도 됨
        if(_isValidUpdate.value == false) {
            _isValidUpdate.value = _isChangeUrl.value
        }
    }

    // 카테고리 변경시 호출
    fun onCheckedChanged(id : Int) {
        when(id) {
            R.id.dishBox ->  {
                _categoryCheckedList.value = getCategoryBooleanList("식당")
                _afterInfoData.value = _afterInfoData.value!!.copy(category = "식당")
             }
            R.id.cafeBox -> {
                _categoryCheckedList.value = getCategoryBooleanList("카페")
                _afterInfoData.value = _afterInfoData.value!!.copy(category = "카페")
            }
            R.id.bakeryBox -> {
                _categoryCheckedList.value = getCategoryBooleanList("빵집")
                _afterInfoData.value = _afterInfoData.value!!.copy(category = "빵집")
            }
            R.id.barBox ->  {
                _categoryCheckedList.value = getCategoryBooleanList("술집")
                _afterInfoData.value = _afterInfoData.value!!.copy(category = "술집")
            }
        }
        checkChangedInfo()

    }

    // 가게 이름 변경시 감지
    fun afterTextChangedPlaceName(s : Editable) {
        _afterInfoData.value = _afterInfoData.value!!.copy(title = s.toString())
        checkChangedInfo()

    }

    fun afterTextChangedAddress2(s : Editable) {
        _afterInfoData.value = _afterInfoData.value!!.copy(address2 = s.toString())
        checkChangedInfo()

    }



    fun updateRestaurantData() {

        var resultPhone = true
        var resultUrl = true
        var resultTimeTable = true
        var resultInfo = true

        viewModelScope.launch {
        // 가게 전화번호 정보 변경
        if(isChangePhone.value == true){
            //viewModelScope.launch {
                updateRestaurantUseCase.updatePhone(_restaurantInfo.value!!.placeId, _restaurantInfo.value!!.title,
                    _restaurantInfo.value!!.phone ?: "", _afterPhoneData.value!!).let {
                        // 없다가 추가될 수도 있음
                        when(it) {
                            is MappingResult.Success<*> -> {
                                resultPhone = true
                            }
                            is MappingResult.Error -> {
                                resultPhone = false
                                //_error.value = it.message ?: "전화번호 정보를 수정하지 못했습니다.\n다시 시도해주세요."
                            }
                        }
                }
            //}
        }

        // 가게 홈페이지 정보 변경
        if(isChangeUrl.value == true){
            //viewModelScope.launch {
                updateRestaurantUseCase.updateUrl(_restaurantInfo.value!!.placeId, _restaurantInfo.value!!.title,
                    _restaurantInfo.value!!.url ?: "", afterHomepageData.value!!).let {
                    // 없다가 추가될 수도 있음
                    when(it) {
                        is MappingResult.Success<*> -> {
                            resultUrl = true
                        }
                        is MappingResult.Error -> {
                            resultUrl = false
                            //_error.value = it.message ?: "홈페이지 정보를 수정하지 못했습니다.\n다시 시도해주세요."
                        }
                    }
                }
            //}
        }

        // 가게 타임테이블 정보 변경
        if(isChangeTime.value == true){
            //viewModelScope.launch {
                updateRestaurantUseCase.updateTime(_restaurantInfo.value!!.placeId,
                    afterTimetableData.value!!.toTimetableUpdating()).let {
                        when(it) {
                            is MappingResult.Success<*> -> {
                                resultTimeTable = true
                            }
                            is MappingResult.Error -> {
                                resultTimeTable = false
                                //_error.value = it.message ?: "운영시간 정보를 수정하지 못했습니다.\n다시 시도해주세요."
                            }
                        }

                }
            //}
        }

        // 가게 기본 정보 변경
        if(isChangeRestaurantInfo.value == true) {

                val infoUpdating : MutableMap<String, Any> = mutableMapOf("placeId" to _restaurantInfo.value!!.placeId,
                    "title" to _restaurantInfo.value!!.title)

                // 가게명 변경 되었는가?
                if(restaurantInfo.value!!.title != afterInfoData.value!!.title) {
                    infoUpdating["changedTitle"] = BeforeAfterString(_restaurantInfo.value!!.title, afterInfoData.value!!.title)
                }

                // 가게 카테고리 변경 되었는가?
                if(restaurantInfo.value!!.category != afterInfoData.value!!.category) {
                    infoUpdating["category"] = BeforeAfterString(_restaurantInfo.value!!.category, afterInfoData.value!!.category)
                }

                // 가게 주소 변경 되었는가? (하나라도 변경 되었으면 add1, add2 모두 넣음) -> 위치도
                if(restaurantInfo.value!!.address2 != afterInfoData.value!!.address2 || restaurantInfo.value!!.address != afterInfoData.value!!.address) {
                    infoUpdating["address2"] = BeforeAfterString(_restaurantInfo.value!!.address2, afterInfoData.value!!.address2)
                    infoUpdating["address"] = BeforeAfterString(_restaurantInfo.value!!.address, afterInfoData.value!!.address)
                }

                if(restaurantInfo.value!!.address != afterInfoData.value!!.address) {
                    infoUpdating["y"] = BeforeAfterString(_restaurantInfo.value!!.x.toString(), afterInfoData.value!!.x.toString())
                    infoUpdating["x"] = BeforeAfterString(_restaurantInfo.value!!.y.toString(), afterInfoData.value!!.y.toString())
                }

                updateRestaurantUseCase.updateRestaurantInfo(infoUpdating).let{
                    when(it) {
                        is MappingResult.Success<*> -> {
                            // 변경사항 로컬 DB에 바로 반영 -> 없은
                            // 현재 화면에 바로 반영 -> 마커색, 이름, 운영시간
                            // 재실행시 반영 -> 카테고리, 운영시간, 주소
                            // 나중에 반영 -> 전화번호, 홈페이지 링크
                            resultInfo = true
                        }
                        is MappingResult.Error -> {
                            resultInfo = false
                            //_error.value = it.message ?: "가게 정보를 수정하지 못했습니다.\n다시 시도해주세요."
                        }
                    }
                }

            }

            // 에러가 하나라도 있음
            if(!(resultInfo && resultPhone && resultUrl && resultTimeTable)) {

                _error.value = "가게 정보를 수정하지 못했습니다.\n다시 시도해주세요."
            } else {

                AmplitudeUtils.placeEdit(afterInfoData.value!!.title)
                _toast.value = "조금만 기다려주세요!\n관리자가 매일 꼼꼼하게 검수하고 있어요."
            }

        }



    }

}