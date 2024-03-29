package com.android.aviro.domain.usecase.retaurant

import com.android.aviro.data.model.restaurant.*
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.RestaurantRepository
import javax.inject.Inject

class SearchRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
){
    suspend fun getSearchedRestaurantList(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : MappingResult {
        return restaurantRepository.searchRestaurant(keyword, x, y, page, size, sort)
    }

    /*
    suspend fun getSearchedRestaurantVeganList(SearchedPlaceRawList : List<Document>) : Result<List<SearchedRestaurantItem>> {
        return restaurantRepository.getVeganTypeOfSearching(SearchedPlaceRawList)

    }*/
}