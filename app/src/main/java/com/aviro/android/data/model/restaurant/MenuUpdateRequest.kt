package com.aviro.android.data.model.restaurant

import com.aviro.android.data.model.menu.MenuDTO

data class MenuUpdateRequest(
    val placeId : String,
    val userId : String,
    val allVegan : Boolean,
    val someMenuVegan : Boolean,
    val ifRequestVegan : Boolean,
    val deleteArray : List<String>,
    val updateArray : List<MenuDTO>, // 수정된 메뉴
    val insertArray : List<MenuDTO>  // 새로 추가된 메뉴
)
