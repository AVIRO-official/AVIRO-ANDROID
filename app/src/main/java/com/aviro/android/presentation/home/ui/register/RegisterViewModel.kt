package com.aviro.android.presentation.home.ui.register

import android.util.Log
import androidx.lifecycle.*
import com.aviro.android.R
import com.aviro.android.domain.entity.search.SearchedRestaurantItem
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.member.MemberLevelUp
import com.aviro.android.domain.entity.menu.Menu
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import com.aviro.android.domain.usecase.retaurant.CreateRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val createRestaurantUseCase : CreateRestaurantUseCase,
    private val getMyInfoUseCase : GetMyInfoUseCase
    )
    : ViewModel() {

    private val _veganTypeList = MutableLiveData<List<Boolean>>()
    val veganTypeList: LiveData<List<Boolean>> = _veganTypeList

    private val _radioCheckedList = MutableLiveData<List<Boolean>>()
    val radioCheckedList: LiveData<List<Boolean>> = _radioCheckedList

    val _registerRestaurant = MutableLiveData<SearchedRestaurantItem>() // 가게 리스트
    var registerRestaurant : LiveData<SearchedRestaurantItem> = _registerRestaurant

    // 메뉴 id로 MenuItem 저장
    var _menuList = MutableLiveData<HashMap<String, Menu>>()
    var menuList: LiveData<HashMap<String, Menu>> = _menuList
    //private val menuList : Map<String, MenuItem> = HashMap<String, MenuItem>()

    private val _isRequest = MutableLiveData<Boolean>()
    val isRequest: LiveData<Boolean> = _isRequest

    private val _levelUp = MutableLiveData<MemberLevelUp>()
    val levelUp: LiveData<MemberLevelUp> get() = _levelUp

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    private lateinit var category : String

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

        val hasTrueValue = _veganTypeList.value?.any { it == true } == true &&
                _radioCheckedList.value?.any { it == true } == true &&
                menuList.value?.all {
                    // 타입, 메뉴명, 가격 있는지 확인
                    // 타입이 need to request면 howToRequest 있는지도 확인
                   val isConditionMet = it.value.menu != "" &&
                            it.value.menuType != "" &&
                            it.value.price != "" &&
                           ((it.value.menuType == "need to request" && it.value.howToRequest != "") || (it.value.menuType != "need to request" && it.value.howToRequest == "" ))
                            //(it.value.menuType != "need to request" || it.value.howToRequest != "")
                    isConditionMet
                } ?: false


        _isRegisterEnabled.value = hasTrueValue


        // 타입이 일부 + 요청 인데, 요청사항 하나라도 없으면 아웃
        if(_veganTypeList.value!![1] && _veganTypeList.value!![2]) {
            val isMenuValid = menuList.value?.any {
                 (it.value.menuType == "need to request" && it.value.howToRequest != "")
            } ?: false

            val isMenuValid2 = menuList.value?.any {
                (it.value.menuType == "vegan" )
            } ?: false

            _isRegisterEnabled.value = (hasTrueValue && isMenuValid && isMenuValid2)

        } else if((!_veganTypeList.value!![1]) && _veganTypeList.value!![2]) {
            val isMenuValid = menuList.value?.all {
                (it.value.menuType == "need to request" && it.value.howToRequest != "")
            } ?: false
            _isRegisterEnabled.value = (hasTrueValue && isMenuValid)
        }

    }


    fun onClickGreen() {
        if(_veganTypeList.value!!.get(0)) {
            _veganTypeList.value = listOf(false, false, false)
        } else {
            _veganTypeList.value = listOf(true, false, false)
        }
        _isRequest.value = false
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

    fun onCheckedChanged(id : Int) { //radio : RadioGroup,
        when(id) {
            R.id.dishBox ->  { _radioCheckedList.value = listOf(true, false, false, false)
                category = "식당" }
            R.id.cafeBox -> { _radioCheckedList.value = listOf(false, true, false, false)
                category = "카페" }
            R.id.bakeryBox -> { _radioCheckedList.value = listOf(false, false, true, false)
            category = "빵집" }
            R.id.barBox ->  { _radioCheckedList.value = listOf(false, false, false, true)
        category = "술집" }
        }

    }



    fun onClickRegister() {
        if(isRegisterEnabled.value == true) {
            // 등록하기 request DTO 로 묶기
            val menuArray = mutableListOf<Menu>()
            menuList.value!!.forEach {
                Log.d("String,Menu","${it.value}")
                menuArray.add(it.value)
            }
            viewModelScope.launch {
                getMyInfoUseCase.getUserId().let {
                    when(it) {
                        is MappingResult.Success<*> -> {
                            val restaurantID = UUID.randomUUID().toString()
                            val userId = it.data.toString()
                            // 등록하기 api 호출
                            createRestaurantUseCase.invoke(restaurantID, userId, registerRestaurant.value!!.placeName, category,
                            registerRestaurant.value!!.addressName, registerRestaurant.value!!.phone, registerRestaurant.value!!.x.toDouble(),
                            registerRestaurant.value!!.y.toDouble(), _veganTypeList.value!!.get(0),_veganTypeList.value!!.get(1),_veganTypeList.value!!.get(2), menuArray).let {

                                when(it) {
                                    is MappingResult.Success<*> -> {
                                        // 레벨업 여부 확인
                                        if(it.data != null) {
                                            _levelUp.value = it.data as MemberLevelUp
                                        }
                                    }
                                    is MappingResult.Error -> _errorLiveData.value = it.message
                                }
                            }
                        }
                        is MappingResult.Error -> _errorLiveData.value = it.message
                    }

                }


            }

        }

    }

}