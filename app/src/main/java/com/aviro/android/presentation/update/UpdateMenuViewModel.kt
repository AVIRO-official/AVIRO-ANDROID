package com.aviro.android.presentation.update

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.common.AmplitudeUtils
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.menu.Menu
import com.aviro.android.domain.entity.restaurant.MenuUpdating
import com.aviro.android.domain.usecase.retaurant.UpdateRestaurantUseCase
import com.aviro.android.presentation.entity.RestaurantInfoForUpdateEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateMenuViewModel @Inject constructor (
    private val updateRestaurantUseCase: UpdateRestaurantUseCase
) : ViewModel() {

    var _restaurantInfo = MutableLiveData<RestaurantInfoForUpdateEntity>()
    val restaurantInfo : LiveData<RestaurantInfoForUpdateEntity>
        get() = _restaurantInfo

    var _isFirstSetting = MutableLiveData<Boolean>()
    val isFirstSetting : LiveData<Boolean>
        get() = _isFirstSetting


    private val _beforeVeganTypeList = MutableLiveData<List<Boolean>>()
    val beforeVeganTypeList: LiveData<List<Boolean>> = _beforeVeganTypeList

    private val _afterVeganTypeList = MutableLiveData<List<Boolean>>()
    val afterVeganTypeList: LiveData<List<Boolean>> = _afterVeganTypeList

    var _isRequest = MutableLiveData<Boolean>() // 요청시 비건 가능 타입인지
    val isRequest : LiveData<Boolean>
        get() = _isRequest


    var _updateMenuList = MutableLiveData<HashMap<String, Menu>>()
    var updateMenuList: LiveData<HashMap<String, Menu>> = _updateMenuList

    var _newMenuList = MutableLiveData<HashMap<String, Menu>>()
    var newMenuList: LiveData<HashMap<String, Menu>> = _newMenuList

    var _deleteList = MutableLiveData<List<String>>()
    var deleteList: LiveData<List<String>> = _deleteList




    var _toastLiveDate = MutableLiveData<String?>()
    val toastLiveDate : LiveData<String?>
        get() = _toastLiveDate

    var _errorLiveData = MutableLiveData<String?>()
    val errorLiveData : LiveData<String?>
        get() = _errorLiveData



    // 버튼 활성화 여부를 나타내는 LiveData
    private val _isUpdateEnable = MediatorLiveData<Boolean>().apply {
        // _veganTypeList 또는 _radioCheckedList 값이 변경될 때마다 호출
        addSource(_afterVeganTypeList) { updateButtonState() }
        addSource(_newMenuList) { updateButtonState() }
        addSource(_updateMenuList) { updateButtonState() }
        addSource(_deleteList) { updateButtonState() }
    }
    val isUpdateEnable: LiveData<Boolean> = _isUpdateEnable

    init {
        _isFirstSetting.value = true
        _isUpdateEnable.value = false
        //_isRequest.value = false
    }

    private fun updateButtonState() {

        // 기존 메뉴(삭제된 것 제외)
        val originMenuOnlyUpdate = restaurantInfo.value?.menuArray?.filter { menuItem ->
            updateMenuList.value?.containsKey(menuItem.menuId) == true
        } ?: emptyList()


        val updatedMenus = updateMenuList.value?.filter { (key, value) ->
            originMenuOnlyUpdate.none { it.menuId == key && it == value } == true
        }

        // 삭제, 수정, 추가 중 하나라도 있어야 함
        val isChangedUpdate = updatedMenus?.isNotEmpty() ?: false
        val isChangedVeganType = ((_beforeVeganTypeList.value != _afterVeganTypeList.value) && (_afterVeganTypeList.value?.any { it == true } == true))
        val isChangedDelete = deleteList.value?.let{ deleteList.value!!.isNotEmpty() } ?: false
        val isChangedInsert = newMenuList.value?.let{newMenuList.value!!.isNotEmpty()} ?: false

        if(isChangedVeganType || isChangedDelete || isChangedUpdate || isChangedInsert) {
            _isUpdateEnable.value = true

            if (isChangedUpdate) {
                updatedMenus?.let {
                    _isUpdateEnable.value = (updateMenuList.value?.all {
                        // 타입, 메뉴명, 가격 있는지 확인
                        // 타입이 need to request면 howToRequest 있는지도 확인
                        val isConditionMet = it.value.menu != "" &&
                                it.value.menuType != "" &&
                                it.value.price != "" &&
                                ((it.value.menuType == "need to request" && it.value.howToRequest != "") || (it.value.menuType != "need to request" && it.value.howToRequest == ""))
                        isConditionMet
                    } == true)
                }
            }




            // 새로 추가된 메뉴
            if (isChangedInsert) {
                _isUpdateEnable.value = (newMenuList.value?.all {
                    // 타입, 메뉴명, 가격 있는지 확인
                    // 타입이 need to request면 howToRequest 있는지도 확인
                    val isConditionMet = it.value.menu != "" &&
                            it.value.menuType != "" &&
                            it.value.price != "" &&
                            ((it.value.menuType == "need to request" && it.value.howToRequest != "") || (it.value.menuType != "need to request" && it.value.howToRequest == ""))
                    isConditionMet
                } == true)
            }



            // 타입이 일부 + 요청 -> 요청사항 하나 이상, 비건 하나 이상
            if (_afterVeganTypeList.value!![1] && _afterVeganTypeList.value!![2]) {
                val isMenuValid = newMenuList.value?.any {
                    (it.value.menuType == "need to request") // && it.value.howToRequest != ""
                } ?: false || updateMenuList.value?.any {
                    (it.value.menuType == "need to request") //&& it.value.howToRequest != ""
                } ?: false

                val isMenuValid2 = newMenuList.value?.any {
                    (it.value.menuType == "vegan") // && it.value.howToRequest != ""
                } ?: false || updateMenuList.value?.any {
                    (it.value.menuType == "vegan") //&& it.value.howToRequest != ""
                } ?: false

                _isUpdateEnable.value =  _isUpdateEnable.value == true && isMenuValid && isMenuValid2

            } else if((!_afterVeganTypeList.value!![1]) && _afterVeganTypeList.value!![2]){
                // 요청사항
                val isMenuValid = newMenuList.value?.all {
                    (it.value.menuType == "need to request" && it.value.howToRequest != "") // && it.value.howToRequest != ""
                } ?: false && updateMenuList.value?.all {
                    (it.value.menuType == "need to request" && it.value.howToRequest != "") //&& it.value.howToRequest != ""
                } ?: false
                _isUpdateEnable.value = _isUpdateEnable.value == true && isMenuValid
            }

            // 비건 타입 하나도 선택 안 된 경우 한번더 확인
            if(_afterVeganTypeList.value?.any { it == true } == false) {
                _isUpdateEnable.value = false
            }
        } else {
            _isUpdateEnable.value = false
        }


    }



    fun setVeganType() {
        _beforeVeganTypeList.value = listOf(_restaurantInfo.value!!.allVegan, _restaurantInfo.value!!.someMenuVegan, _restaurantInfo.value!!.ifRequestVegan)
        _afterVeganTypeList.value = listOf(_restaurantInfo.value!!.allVegan, _restaurantInfo.value!!.someMenuVegan, _restaurantInfo.value!!.ifRequestVegan)
    }


    fun updateMenu() {
        viewModelScope.launch {

            val updateArray = _updateMenuList.value?.map{ it.value } ?: emptyList()
            val insertArray = _newMenuList.value?.map{ it.value } ?: emptyList()
            val deleteArray = _deleteList.value ?: emptyList()

            updateRestaurantUseCase.updateMenu(MenuUpdating(_restaurantInfo.value!!.placeId, _afterVeganTypeList.value!![0], _afterVeganTypeList.value!![1],_afterVeganTypeList.value!![2],
                deleteArray, updateArray, insertArray)).let {
                when(it) {
                    is MappingResult.Success<*> -> {

                        // 앰플리튜드 전송
                        val afterMenuArray = setUpdatedMenuList(updateArray, insertArray, deleteArray)
                        AmplitudeUtils.menuEdit(_restaurantInfo.value!!.title, _restaurantInfo.value!!.menuArray, afterMenuArray)

                        // 토스트 메세지 띄우기
                        _toastLiveDate.value = it.message ?: "소중한 정보 감사해요.\n수정해주신 정보로 업데이트 되었어요!"
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message

                    }
                }

            }

        }

    }

    fun deleteMenu(menuID : String) {
        val newDeleteList = _deleteList.value?.toMutableList() ?: mutableListOf()
        newDeleteList.add(menuID)
        _deleteList.value = newDeleteList
    }


    fun onClickGreen() {
        if(_afterVeganTypeList.value!![0]) {
            _afterVeganTypeList.value = listOf(false, false, false)
        } else {
            _afterVeganTypeList.value = listOf(true, false, false)
        }

        _isRequest.value = false
    }

    fun onClickOrange() {
        if(_afterVeganTypeList.value!![1]) {
            _afterVeganTypeList.value = listOf(false, false, _afterVeganTypeList.value!![2])
        } else {
            _afterVeganTypeList.value = listOf(false, true, _afterVeganTypeList.value!![2])
        }


    }
    fun onClickYellow() {
        if(_afterVeganTypeList.value!![2]) {
            _afterVeganTypeList.value = listOf(false, _afterVeganTypeList.value!![1], false)
        } else {
            _afterVeganTypeList.value = listOf(false, _afterVeganTypeList.value!![1], true)
        }
        _isRequest.value = _afterVeganTypeList.value!![2]
    }

    fun setUpdatedMenuList(updateMenuList : List<Menu>, insertMenuList : List<Menu>, deleteMenuList : List<String>) : List<Menu> {
        var afterMenuList = mutableListOf<Menu>()

        _restaurantInfo.value!!.menuArray.forEach { before ->
            if(deleteMenuList.filter { it == before.menuId }.isEmpty()) {

                // 업데이트된 경우
                val updatedMenu = updateMenuList.find { it.menuId == before.menuId }

                // 추가된 경우
                val insertedMenu = insertMenuList.find { it.menuId == before.menuId }


                if(updatedMenu != null) {
                    afterMenuList.add(updatedMenu)

                } else if(insertedMenu != null) {
                    afterMenuList.add(insertedMenu)
                } else {
                    // 변화 없는 경우
                    afterMenuList.add(before)
                }

            }
        }

        return afterMenuList

    }


}