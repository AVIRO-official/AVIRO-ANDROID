package com.android.aviro.presentation.entity

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (

    @SerializedName("photoSpotId")
    val userId : Int,
    val userName : String,
    val userEmail : String,
    var nickname : String,
    var birthday : Int?,
    var gender : String?, //ENUM ('female', 'male', 'other')
    var marketingAgree : Boolean
)
