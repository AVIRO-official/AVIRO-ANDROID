package com.android.aviro.presentation.home.ui.register

import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.aviro.R
import com.android.aviro.domain.entity.MenuEntity

class RegisterViewModel : ViewModel() {

    private val _veganTypeList = MutableLiveData<List<Boolean>>()
    val veganTypeList: LiveData<List<Boolean>> = _veganTypeList

    private val _radioCheckedList = MutableLiveData<List<Boolean>>()
    val radioCheckedList: LiveData<List<Boolean>> = _radioCheckedList

    private val _restaurantAddress = MutableLiveData<String?>()
    val restaurantAddress: LiveData<String?> = _restaurantAddress

    private val _restaurantNumber = MutableLiveData<String?>()
    val restaurantNumber: LiveData<String?> = _restaurantNumber

    // 메뉴 id로 MenuItem 저장
    var _menuList = MutableLiveData<HashMap<String, MenuEntity>>()
    var menuList: LiveData<HashMap<String, MenuEntity>> = _menuList
    //private val menuList : Map<String, MenuItem> = HashMap<String, MenuItem>()

    private val _isRequest = MutableLiveData<Boolean>()
    val isRequest: LiveData<Boolean> = _isRequest

    // 버튼 활성화 여부를 나타내는 LiveData
    private val _isRegisterEnabled = MediatorLiveData<Boolean>().apply {
        // _veganTypeList 또는 _radioCheckedList 값이 변경될 때마다 호출
        addSource(_veganTypeList) { updateButtonState() }
        addSource(_radioCheckedList) { updateButtonState() }
        addSource(_menuList) { updateButtonState() }
    }
    val isRegisterEnabled: LiveData<Boolean> = _isRegisterEnabled


    init {
        _veganTypeList.value = listOf(false, false, false)
        _radioCheckedList.value = listOf(false, false, false, false)
       // _isNext.value = false
        _isRequest.value = false

    }

    private fun updateButtonState() {
        // _veganTypeList 또는 _radioCheckedList에 true 값이 하나라도 있는지 확인
        val hasTrueValue = _veganTypeList.value?.any { it == true } == true &&
                _radioCheckedList.value?.any { it == true } == true &&
                menuList.value?.all {
                    // 타입, 메뉴명, 가격 있는지 확인
                    // 타입이 need to request면 howToRequest 있는지도 확인
                   val isConditionMet = it.value.menu != "" &&
                            it.value.menuType != "" &&
                            it.value.price != "" &&
                            (it.value.menuType != "need to request" || it.value.howToRequest != "")
                    isConditionMet
                } ?: false

        _isRegisterEnabled.value = hasTrueValue
    }


    fun onClickGreen() {
        if(_veganTypeList.value!!.get(0)) {
            _veganTypeList.value = listOf(false, false, false)
        } else {
            _veganTypeList.value = listOf(true, false, false)
        }
    }

    fun onClickOrange() {
        if(_veganTypeList.value!!.get(1)) {
            _veganTypeList.value = listOf(false, false, _veganTypeList.value!!.get(2))
        } else {
            _veganTypeList.value = listOf(false, true, _veganTypeList.value!!.get(2))
        }
    }

    fun onClickYellow() {
        if(_veganTypeList.value!!.get(2)) {
            _veganTypeList.value = listOf(false, _veganTypeList.value!!.get(1), false)
        } else {
            _veganTypeList.value = listOf(false, _veganTypeList.value!!.get(1), true)
        }
        _isRequest.value = _veganTypeList.value!!.get(2)
    }

    fun onCheckedChanged(radio : RadioGroup, id : Int) {
        when(id) {
            R.id.dishBox -> _radioCheckedList.value = listOf(true, false, false, false)
            R.id.cafeBox -> _radioCheckedList.value = listOf(false, true, false, false)
            R.id.bakeryBox -> _radioCheckedList.value = listOf(false, false, true, false)
            R.id.barBox -> _radioCheckedList.value = listOf(false, false, false, true)
        }

    }

    fun onClickRequestCheckBox() {
        _isRequest.value = if (_isRequest.value == true)  false else true
    }


/*
    fun onCheckedRegisterBtn() : Boolean? {

        // menuItemList 확인 작업
        menuList.value?.map {
            // 타입, 메뉴명, 가격 있는지 확인
            // 타입이 need to request면 howToRequest 있는지도 확인
            if(it.value.menu != "" && it.value.menuType != "vegan" && it.value.price != "") {
                _isNext.value = it.value.menuType == "need to request" && it.value.howToRequest != ""
            } else {
                _isNext.value = false
                return false
            }
        }

        // 카테고리 체크 여부 확인
        _isNext.value = if(_radioCheckedList.value?.any { it == true } ?: false) true else false


        // 비건 타입 체크 여부 확인
        _isNext.value = if(_veganTypeList.value?.any { it == true } ?: false) true else false

        // 가게 선택 여부 확인

        return _isNext.value
    }

 */

    fun onClickRegister() {
        Log.d("onClickRegister","${isRegisterEnabled.value}")
        if(isRegisterEnabled.value == true) {

            // 등록하기 request DTO 로 묶기
            // 등록하기 api 호출
        }

    }

}