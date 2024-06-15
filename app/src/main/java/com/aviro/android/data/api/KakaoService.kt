package com.aviro.android.data.api


import com.aviro.android.data.model.restaurant.SearchedListFromKakaoResponse
import com.aviro.android.data.model.kakao.address.AddressOfCoordFromKakaoResponse
import com.aviro.android.data.model.kakao.coordi.CoordOfAddressFromKakaoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {

    @GET("search/keyword.json")
    suspend fun searchRestaurant(
        @Query("query") query : String,
        @Query("x") x : String?,
        @Query("y") y : String?,
        @Query("category_group_code") category_group_code : String, // FD6=음식점 / CE7=카페
        @Query("page") page : Int,
        @Query("size") size : Int,   //한 페이지에 보여질 문서의 개수
        @Query("sort") sort : String
    ) : Result<SearchedListFromKakaoResponse>

    @GET("search/address.json")
    suspend fun getCoordination(
        @Query("query") query : String,
    ) : Result<CoordOfAddressFromKakaoResponse>

    @GET("geo/coord2address.json")
    suspend fun getRoadAddress(
        @Query("x") x : String,
        @Query("y") y : String,
    ) : Result<AddressOfCoordFromKakaoResponse>


}