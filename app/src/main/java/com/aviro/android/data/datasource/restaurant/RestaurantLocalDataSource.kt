package com.aviro.android.data.datasource.restaurant

import com.aviro.android.data.model.restaurant.RestaurantDAO

interface RestaurantLocalDataSource {

    suspend fun saveRestaurant(RestaurantList : List<RestaurantDAO>)
    suspend fun updateRestaurant(update_list : List<RestaurantDAO>)
    suspend fun deleteRestaurant(delete_list : List<String>)
    suspend fun getRestaurant() : List<RestaurantDAO>
    suspend fun getRestaurantId() : List<String>
    fun closeRealm()


}