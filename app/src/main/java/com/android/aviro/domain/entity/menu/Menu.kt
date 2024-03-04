package com.android.aviro.domain.entity.menu

data class Menu(
    val menuId : String,
    var menuType : String,
    var menu : String,
    var price : String,
    var howToRequest : String?,
    val isCheck : Boolean
)
