package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.data.model.restaurant.*
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.RestaurantRepository
import javax.inject.Inject

class SearchRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
){
    suspend fun getSearchedRestaurantList(keyword : String, x : String?, y : String?, page : Int, size : Int, sort : String) : MappingResult {
        return restaurantRepository.searchRestaurant(keyword, x, y, page, size, sort)
    }

    suspend fun getIsRegistered(title : String, address : String, x : Double, y :Double) : MappingResult{
        return restaurantRepository.getIsRegistered(title, address, x, y)
    }

    /*
    suspend fun getSearchedRestaurantVeganList(SearchedPlaceRawList : List<Document>) : Result<List<SearchedRestaurantItem>> {
        return restaurantRepository.getVeganTypeOfSearching(SearchedPlaceRawList)

    }*/
}