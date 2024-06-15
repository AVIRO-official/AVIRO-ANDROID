package com.aviro.android.presentation.update

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.search.CoordiOfPublicAddress
import com.aviro.android.domain.entity.search.PublicAddressItem
import com.aviro.android.domain.entity.search.PublicAddressList
import com.aviro.android.domain.entity.search.RoadAddressOfCoordi
import com.aviro.android.domain.usecase.retaurant.SearchLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAddressViewModel @Inject constructor (
    private val searchLocationUseCase: SearchLocationUseCase
) : ViewModel()  {

    var _newAddressPage = MutableLiveData<PublicAddressList>()
    val newAddressPage : LiveData<PublicAddressList>
        get() = _newAddressPage

    var _addressItemList = MutableLiveData<List<PublicAddressItem>>()
    val addressItemList : LiveData<List<PublicAddressItem>>
        get() = _addressItemList


    var _searchedKeyword = MutableLiveData<String>()
    val searchedKeyword : LiveData<String>
        get() = _searchedKeyword

    var _isSearchingAddress = MutableLiveData<Boolean>()
    val isSearchingAddress : LiveData<Boolean>
        get() = _isSearchingAddress


    var _isAddressResult = MutableLiveData<Boolean>()
    val isAddressResult : LiveData<Boolean>
        get() = _isAddressResult



    var _coordiOfPublicAddress = MutableLiveData<CoordiOfPublicAddress?>()
    val coordiOfPublicAddress : LiveData<CoordiOfPublicAddress?>
        get() = _coordiOfPublicAddress


    var _addressOfMap = MutableLiveData<String?>()
    val addressOfMap : LiveData<String?>
        get() = _addressOfMap

    var isProgress = false

    var totalAmount = 0
    var currentPage = 1
    var currentAmount = 0

    var _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error


    init {
        _isSearchingAddress.value = false
        _isAddressResult.value = false
    }


    // 가게 기본 주소 변경 - 공공 주소 검색
    fun searchPublicAddress(keyword : String, page : Int) {
        isProgress = true
        viewModelScope.launch {
            searchLocationUseCase(keyword, page).let{
                when(it) {
                    is MappingResult.Success<*> -> {
                        if(it.data != null) {
                            val data = it.data as PublicAddressList

                            Log.d("publicAddressList","${data.publicAddressList}")

                            if(currentPage == 1) {
                                totalAmount = data.totalCount.toInt()
                                _addressItemList.value = data.publicAddressList
                            } else {
                                _addressItemList.value = _addressItemList.value?.plus(data.publicAddressList)
                            }

                            currentPage ++
                            currentAmount += data.countPerPage.toInt()

                            isProgress = false

                        }
                    }

                    is MappingResult.Error -> {
                            _error.value = it.message
                    }
                }
            }
        }
    }

    fun getCoordination(address : String) {
        viewModelScope.launch {
            searchLocationUseCase.getCoordinationOfAddress(address).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as CoordiOfPublicAddress
                        _coordiOfPublicAddress.value = data
                    }

                    is MappingResult.Error -> {
                        // 선택한 주소의 좌표 없는 경우애도 뜸
                        _coordiOfPublicAddress.value = null
                        _error.value = it.message
                    }
                }
            }
        }
    }

    fun getRoadAddress(x : Double, y : Double) {
        viewModelScope.launch {
            searchLocationUseCase.getRoadAddressOfCoordination(y, x).let {
                Log.d("getRoadAddress","${it}")
                when(it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as RoadAddressOfCoordi
                        if(data.road_address == "") {
                            // 등록 버튼 비활성화
                            _addressOfMap.value = null
                        } else {
                            _addressOfMap.value = data.road_address
                        }
                    }

                    is MappingResult.Error -> {
                        _error.value = it.message
                    }
                }
            }
        }
    }

    // 공공 주소 검색 변화 감지
    fun onTextChangedAddressOnPublic(s : Editable) {
        // 공공 주소 검색
        _searchedKeyword.value = s.toString()
        currentPage = 1
        currentAmount = 0
        searchPublicAddress(_searchedKeyword.value!!, currentPage)
    }

    // 공공 주소 검색중인지 확인 (포커스 여부)
    fun onEditTextFocusChanged(hasFocus : Boolean) {  //editTextView : View,
        _isSearchingAddress.value = hasFocus
    }


}