package com.android.aviro.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//@Parcelize
data class SignInRequest(

    //@SerializedName("refreshToken")
    val refreshToken : String

    ) //: Parcelable