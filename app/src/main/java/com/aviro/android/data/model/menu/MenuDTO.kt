package com.aviro.android.data.model.menu

data class MenuDTO(
    val menuId : String,
    var menuType : String,
    var menu : String,
    var price : String,
    var howToRequest : String?,
    val isCheck : Boolean
)
