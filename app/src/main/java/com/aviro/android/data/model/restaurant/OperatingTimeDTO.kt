package com.aviro.android.data.model.restaurant

data class OperatingTimeDTO(
    val today : Boolean,
    val open : String,
    val `break` : String
)
//@SerialName("`break`")