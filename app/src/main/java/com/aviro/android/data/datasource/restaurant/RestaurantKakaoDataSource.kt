package com.aviro.android.data.datasource.restaurant

import com.aviro.android.data.model.restaurant.SearchedListFromKakaoResponse
import com.aviro.android.data.model.kakao.address.AddressOfCoordFromKakaoResponse
import com.aviro.android.data.model.kakao.coordi.CoordOfAddressFromKakaoResponse


interface RestaurantKakaoDataSource {
    suspend fun getSearchedRestaurant(keyword : String, x : String?, y : String?, page : Int, size : Int, sort : String) : Result<SearchedListFromKakaoResponse>

    suspend fun getCoordinationOfAddress(keyword : String) : Result<CoordOfAddressFromKakaoResponse>
    suspend fun getAddressOfCoordination(x : String, y : String) : Result<AddressOfCoordFromKakaoResponse>
}