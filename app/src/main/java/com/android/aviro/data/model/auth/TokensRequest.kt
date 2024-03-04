package com.android.aviro.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//@Parcelize
data class TokensRequest(

    //@SerializedName("identityToken")
    val id_token : String,
    //@SerializedName("authorizationCode")
    val auth_code : String

) //: Parcelable

