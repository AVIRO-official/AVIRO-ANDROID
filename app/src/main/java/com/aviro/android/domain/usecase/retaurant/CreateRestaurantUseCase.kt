package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.menu.Menu
import com.aviro.android.domain.entity.restaurant.Restaurant
import com.aviro.android.domain.repository.RestaurantRepository
import javax.inject.Inject

class CreateRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
){

    suspend fun invoke(placeId : String, userId : String, title : String, category : String, address : String, phone : String?,
    x : Double, y : Double, allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean, menuArray : List<Menu>) : MappingResult {
        return restaurantRepository.createRestaurant(Restaurant(placeId, userId, title, category, address, phone, x, y, allVegan,
        someMenuVegan, ifRequestVegan, menuArray))

    }
}