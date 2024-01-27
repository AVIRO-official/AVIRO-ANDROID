package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.entity.restaurant.LocOfRestaurant
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.query.RealmResults

interface RestaurantLocalDataSource {

    fun saveRestaurant(RestaurantList : List<LocOfRestaurant>)
    fun updateRestaurant(update_list : List<LocOfRestaurant>)
    fun deleteRestaurant(delete_list : List<String>)
    fun getRestaurant() : List<LocOfRestaurant>
    fun getRestaurantId() : List<String>
    fun closeRealm()


}