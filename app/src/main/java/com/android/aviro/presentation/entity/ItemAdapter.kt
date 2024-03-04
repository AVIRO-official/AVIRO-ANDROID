package com.android.aviro.presentation.entity

import com.android.aviro.domain.entity.SearchedRestaurantItem

data class ItemAdapter (

    var isNewKeyword: Boolean,
    var itemList: List<SearchedRestaurantItem>,
    var addingCount : Int

)