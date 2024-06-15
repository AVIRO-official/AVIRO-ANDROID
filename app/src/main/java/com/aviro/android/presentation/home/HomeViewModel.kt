package com.aviro.android.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aviro.android.domain.entity.member.MyComment
import com.aviro.android.domain.entity.member.MyRestaurant

class HomeViewModel : ViewModel()  {

    enum class WhereToGo {
        REVIEW, RESTAURANT, BOOKMARK, REGISTER, MYPAGE
    }


    var _currentNavigation = MutableLiveData<WhereToGo>() // 마이페이지에서 어떤 목록인지
    val currentNavigation : LiveData<WhereToGo>
        get() = _currentNavigation

    var _isNavigation = MutableLiveData<Boolean>() // 지금 이동해서 온건지 아닌지를 확인
    val isNavigation : LiveData<Boolean>
        get() = _isNavigation

    var _reviewData = MutableLiveData<MyComment>()
    val reviewData : LiveData<MyComment>
        get() = _reviewData

    var _restaurantData = MutableLiveData<MyRestaurant>()
    val restaurantData : LiveData<MyRestaurant>
        get() = _restaurantData


    init {
        _isNavigation.value = false
    }



    fun setReviewData(comment : MyComment) {
        _reviewData.postValue(comment)
    }

    fun setRestaurantData(restaurant : MyRestaurant) {
        _restaurantData.postValue(restaurant)
    }




}