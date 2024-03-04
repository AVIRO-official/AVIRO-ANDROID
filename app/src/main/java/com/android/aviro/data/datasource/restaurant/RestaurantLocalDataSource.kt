package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.model.restaurant.RestaurantDAO

interface RestaurantLocalDataSource {

    fun saveRestaurant(RestaurantList : List<RestaurantDAO>)
    fun updateRestaurant(update_list : List<RestaurantDAO>)
    fun deleteRestaurant(delete_list : List<String>)
    fun getRestaurant() : List<RestaurantDAO>
    fun getRestaurantId() : List<String>
    fun closeRealm()


}