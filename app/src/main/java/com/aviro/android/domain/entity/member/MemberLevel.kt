package com.aviro.android.domain.entity.member

import java.net.URL

data class MemberLevel(

    val level: Int,
    val point: Int,
    val pointForNext: Int,
    val total: Int,
    val userRank: Int,
    val image: URL
)
