package com.android.aviro.presentation.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GuideViewModel(fragmentId: String) : ViewModel() {

    val _dot = MutableLiveData<List<Boolean>>()
    val dot : LiveData<List<Boolean>>
        get() = _dot


    init {
        when(fragmentId) {
            "menu" -> _dot.value = listOf(true, false, false, false)
            "register" -> _dot.value = listOf(false, true, false, false)
            "search" -> _dot.value = listOf(false, false, true, false)
            "review" -> _dot.value = listOf(false, false, false, true)
        }

    }

}