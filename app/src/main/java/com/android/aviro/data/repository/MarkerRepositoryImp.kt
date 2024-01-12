package com.android.aviro.data.repository

import com.android.aviro.data.datasource.marker.MarkerCacheDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSource
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.domain.repository.MarkerRepository
import javax.inject.Inject

class MarkerRepositoryImp @Inject constructor (
    private val markerCacheDataSource: MarkerCacheDataSource
) : MarkerRepository {

   override fun getMarker() : MarkerListEntity {
       return markerCacheDataSource.getMarker()
    }

}