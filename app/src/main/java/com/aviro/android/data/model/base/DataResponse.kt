package com.aviro.android.data.model.base

data class DataResponse<T>(

    //@SerializedName("statusCode")
    val statusCode : Int,

    //@SerializedName("message")
    val message : String?,

    //@SerializedName("data")
    val data: T?

)