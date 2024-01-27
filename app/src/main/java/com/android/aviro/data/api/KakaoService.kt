package com.android.aviro.data.api

import retrofit2.http.GET

interface KakaoService {

    @GET("keyword")
    suspend fun searchRestaurant(

    )
}