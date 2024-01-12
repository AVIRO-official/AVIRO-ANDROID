package com.android.aviro.domain.repository

import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.domain.entity.MemberEntity

interface MarkerRepository {

    fun getMarker() : MarkerListEntity
}