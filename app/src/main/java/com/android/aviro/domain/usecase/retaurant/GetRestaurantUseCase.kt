package com.android.aviro.domain.usecase.retaurant

import android.content.Context
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import com.android.aviro.domain.repository.MarkerRepository
import com.android.aviro.domain.repository.RestaurantRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository,
    private val markerRepository: MarkerRepository
){

    operator suspend fun invoke(x : String, y : String, wide : String, time : String)  {
       return  restaurantRepository.getRestaurant(request = RestaurantRequestDTO(x, y, wide, time))
    }

    fun getMarker() : MarkerListEntity {
        return markerRepository.getMarker()
    }

}