package com.android.aviro.data.entity.member

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NicknameEntity(

    @SerializedName("nickname")
    val nickname: String,

): Parcelable
