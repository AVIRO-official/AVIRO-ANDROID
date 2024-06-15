package com.aviro.android.domain.entity.member

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberLevelUp(
    val levelUp : Boolean,
    val userLevel : Int
) : Parcelable
