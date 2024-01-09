package com.android.aviro.data.entity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokensRequestDTO(

    @SerializedName("identityToken")
    val id_token : String,
    @SerializedName("authorizationCode")
    val auth_code : String

) : Parcelable

