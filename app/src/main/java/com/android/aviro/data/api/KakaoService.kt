package com.android.aviro.data.api

import com.android.aviro.data.model.restaurant.SearchedListFromKakaoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {

    @GET("keyword.json")
    suspend fun searchRestaurant(
        @Query("query") query : String,
        @Query("x") x : String,
        @Query("y") y : String,
        //@Query("category_group_code") category_group_code : String, // FD6=음식점 / CE7=카페
        @Query("page") page : Int, // ?? 결과 페이지 번호
        @Query("size") size : Int, // 한 페이지에 보여질 문서의 개수
        @Query("sort") sort : String,  //Enum("accuracy", "distance")

    ) : Result<SearchedListFromKakaoResponse>
}