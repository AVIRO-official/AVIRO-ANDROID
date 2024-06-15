package com.aviro.android.data.api

import com.aviro.android.data.model.search.PublicAddressListResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface PublicService {
    @POST("addrlink/addrLinkApi.do")
    suspend fun searchAddress(
        @Query("confmKey") key : String,
        @Query("keyword") x : String,
        @Query("resultType") type : String,
        @Query("currentPage") page : Int,
        @Query("countPerPage") count : Int
    ): Result<PublicAddressListResponse>

}