package com.android.aviro.presentation.home.ui.register

import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.aviro.R

class RegisterViewModel : ViewModel() {

    private val _veganTypeList = MutableLiveData<List<Boolean>>()
    val veganTypeList: LiveData<List<Boolean>> = _veganTypeList

    private val _radioCheckedList = MutableLiveData<List<Boolean>>()
    val radioCheckedList: LiveData<List<Boolean>> = _radioCheckedList

    private val _restaurantAddress = MutableLiveData<String?>()
    val restaurantAddress: LiveData<String?> = _restaurantAddress

    private val _restaurantNumber = MutableLiveData<String?>()
    val restaurantNumber: LiveData<String?> = _restaurantNumber

    private val _menuList = MutableLiveData<List<LinearLayout>>()
    val menuList: LiveData<List<LinearLayout>> = _menuList

    private val _isRequest = MutableLiveData<Boolean>()
    val isRequest: LiveData<Boolean> = _isRequest

    private val _isNext = MutableLiveData<Boolean>()
    val isNext: LiveData<Boolean> = _isNext


    init {
        _veganTypeList.value = listOf(false, false, false)
        _radioCheckedList.value = listOf(false, false, false, false)
        _isNext.value = false
        _isRequest.value = false
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
    }

    fun onCheckedChanged(radio : RadioGroup, id : Int) {
        when(id) {
            R.id.dishBox ->   _radioCheckedList.value = listOf(true, false, false, false)
            R.id.cafeBox ->   _radioCheckedList.value = listOf(false, true, false, false)
            R.id.bakeryBox ->   _radioCheckedList.value = listOf(false, false, true, false)
            R.id.barBox ->   _radioCheckedList.value = listOf(false, false, false, true)

        }

    }

    fun onClickRequestCheckBox() {
        _isRequest.value = if (_isRequest.value == true)  false else true
    }

    fun onCheckedRegisterBtn() : Boolean? {

        return _isNext.value

    }
}