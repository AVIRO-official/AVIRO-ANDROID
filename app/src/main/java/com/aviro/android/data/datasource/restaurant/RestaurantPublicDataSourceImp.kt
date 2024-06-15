package com.aviro.android.data.datasource.restaurant

import com.aviro.android.data.api.PublicService
import com.aviro.android.data.model.search.PublicAddressListResponse
import javax.inject.Inject

class RestaurantPublicDataSourceImp  @Inject constructor (
    private val publicService: PublicService
) : RestaurantPublicDataSource {
    override suspend fun getSearchedAddress(comfrimKey : String, keyword : String, type : String, page : Int, count : Int) : Result<PublicAddressListResponse> {
        return publicService.searchAddress(comfrimKey, keyword, type, page, count)
    }

}