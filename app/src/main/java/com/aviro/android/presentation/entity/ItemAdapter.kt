package com.aviro.android.presentation.entity

import com.aviro.android.domain.entity.search.SearchedRestaurantItem


data class ItemAdapter (

    var isNewKeyword: Boolean,
    var itemList: List<SearchedRestaurantItem>,
    var addingCount : Int

)