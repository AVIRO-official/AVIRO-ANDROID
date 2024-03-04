package com.android.aviro.data.model.member

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.net.URL

@Parcelize
data class MyInfoLevelResponse(

    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("message") // 챌린지가 없거나 서버쪽 에러일 때만 발생합니다
    val message: String?,

    @SerializedName("level")
    val level: Int?,

    @SerializedName("point")
    val point: Int?,

    @SerializedName("pointForNext")
    val pointForNext: Int?,

    @SerializedName("total")
    val total: Int?,

    @SerializedName("userRank")
    val userRank: Int?,

    @SerializedName("image")
    val image: URL?,


    ) : Parcelable
