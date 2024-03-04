package com.android.aviro.data.model.base

data class BodyResponse<T> (

    val statusCode : Int,

    val message : String?,

    val body: T?

)
