package com.android.aviro.presentation.search

import com.android.aviro.data.entity.restaurant.SearchEntity

data class ItemAdapter (

    var itemList: List<SearchEntity>,
    var isNewKeyword: Boolean
)