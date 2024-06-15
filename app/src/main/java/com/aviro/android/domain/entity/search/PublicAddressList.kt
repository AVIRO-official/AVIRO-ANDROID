package com.aviro.android.domain.entity.search

data class PublicAddressList(
    val totalCount : String,
    val currentPage : String,
    val countPerPage : String,
    val publicAddressList : List<PublicAddressItem>
)
