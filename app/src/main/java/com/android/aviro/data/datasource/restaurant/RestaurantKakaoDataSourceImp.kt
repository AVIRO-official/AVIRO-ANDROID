package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.api.KakaoService
import com.android.aviro.data.api.RestaurantService
import com.android.aviro.data.model.restaurant.SearchedListFromKakaoResponse
import javax.inject.Inject

class RestaurantKakaoDataSourceImp @Inject constructor (
    private val kakaoService: KakaoService
) : RestaurantKakaoDataSource {

    override suspend fun getSearchedRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : Result<SearchedListFromKakaoResponse> {
        return kakaoService.searchRestaurant(keyword, x, y, page, size, sort)
    }

}