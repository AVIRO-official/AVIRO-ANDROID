package com.aviro.android.data.datasource.restaurant

import com.aviro.android.data.model.search.PublicAddressListResponse

interface RestaurantPublicDataSource {
    suspend fun getSearchedAddress(comfrimKey : String, keyword : String, type : String, page : Int, count : Int) : Result<PublicAddressListResponse>

}