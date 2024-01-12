package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.entity.restaurant.LocOfRestaurant
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.query.RealmResults

interface RestaurantLocalDataSource {

    fun saveRestaurant(RestaurantList : List<LocOfRestaurant>)
    fun updateRestaurant()
    fun deleteRestaurant()
    fun getRestaurant() : List<LocOfRestaurant>
    fun closeRealm()


}