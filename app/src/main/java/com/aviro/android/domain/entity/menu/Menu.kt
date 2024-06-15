package com.aviro.android.domain.entity.menu

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(
    val menuId : String,
    var menuType : String,
    var menu : String,
    var price : String,
    var howToRequest : String?,
    val isCheck : Boolean
) : Parcelable
