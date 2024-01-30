package com.android.aviro.domain.usecase.retaurant

import com.android.aviro.data.entity.restaurant.SearchRestaurantResponse
import com.android.aviro.domain.repository.RestaurantRepository
import javax.inject.Inject

class SearchRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
){
    suspend fun getSearchedRestaurantList(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : Result<SearchRestaurantResponse> {
        return restaurantRepository.searchRestaurant(keyword, x, y, page, size, sort)
    }
}