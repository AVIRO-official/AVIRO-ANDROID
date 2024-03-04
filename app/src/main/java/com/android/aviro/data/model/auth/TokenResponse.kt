package com.android.aviro.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//@Parcelize
data class TokenModel(

    //@SerializedName("isMember")
    val isMember: Boolean,

    //@SerializedName("refreshToken")
    val refreshToken: String,

    //@SerializedName("accessToken")
    val accessToken: String,

    //@SerializedName("userId")
    val userId: String

) //: Parcelable

