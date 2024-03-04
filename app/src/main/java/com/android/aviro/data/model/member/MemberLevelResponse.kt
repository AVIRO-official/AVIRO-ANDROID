package com.android.aviro.data.model.member


import java.net.URL

data class MemberLevelResponse(

    val level: Int?,
    val point: Int?,
    val pointForNext: Int?,
    val total: Int?,
    val userRank: Int?,
    val image: URL?
    )
