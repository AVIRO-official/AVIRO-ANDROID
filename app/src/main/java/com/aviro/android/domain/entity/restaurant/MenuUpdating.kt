package com.aviro.android.domain.entity.restaurant

import com.aviro.android.domain.entity.menu.Menu

data class MenuUpdating(
    val placeId : String,
    val allVegan : Boolean,
    val someMenuVegan : Boolean,
    val ifRequestVegan : Boolean,
    val deleteArray : List<String>,
    val updateArray : List<Menu>,
    val insertArray : List<Menu>
)
//  val userId : String,
