package com.aviro.android.data.datasource.restaurant

import com.aviro.android.data.model.restaurant.SearchedListFromKakaoResponse
import com.aviro.android.data.api.KakaoService
import com.aviro.android.data.model.kakao.address.AddressOfCoordFromKakaoResponse
import com.aviro.android.data.model.kakao.coordi.CoordOfAddressFromKakaoResponse
import javax.inject.Inject

class RestaurantKakaoDataSourceImp @Inject constructor (
    private val kakaoService: KakaoService
) : RestaurantKakaoDataSource {

    // 메인 검색(카테고리 다름)
    override suspend fun getSearchedRestaurant(keyword : String, x : String?, y : String?, page : Int, size : Int, sort : String) : Result<SearchedListFromKakaoResponse> {
        return kakaoService.searchRestaurant(keyword, x, y, "CE7, FD6, SW8, AT4, PO3",page, size, sort)
    }


    override  suspend fun getCoordinationOfAddress(keyword : String) : Result<CoordOfAddressFromKakaoResponse> {
        return kakaoService.getCoordination(keyword)
    }
    override suspend fun getAddressOfCoordination(x : String, y : String) : Result<AddressOfCoordFromKakaoResponse> {
        return kakaoService.getRoadAddress(x, y)
    }

}