package com.android.aviro.data.entity.challenge

import com.google.gson.annotations.SerializedName


data class ChallengeInfoResponse(
    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("period")
    val period: String,

    @SerializedName("title")
    val title: String
)
