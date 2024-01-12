package com.android.aviro.data.datasource.restaurant

import android.util.Log
import com.android.aviro.data.api.RestaurantService
import com.android.aviro.data.entity.restaurant.LocOfRestaurant
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import javax.inject.Inject


class RestaurantLocalDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService,
) : RestaurantLocalDataSource {

    lateinit var realm: Realm

    override fun saveRestaurant(RestaurantList : List<LocOfRestaurant>) {
        val config = RealmConfiguration.create(schema = setOf(LocOfRestaurant::class))
        realm = Realm.open(config)

        realm.writeBlocking {
            Log.d("saveRestaurant" , "start save")
            RestaurantList.forEach {
                copyToRealm(LocOfRestaurant().apply {
                    placeId = it.placeId
                    x = it.x
                    y = it.y
                    allVegan= it.allVegan
                    someMenuVegan= it.someMenuVegan
                    ifRequestVegan= it.ifRequestVegan

                })
            }
        }
        closeRealm()

    }

    override fun getRestaurant() : List<LocOfRestaurant> {
        val config = RealmConfiguration.create(schema = setOf(LocOfRestaurant::class))
        realm= Realm.open(config)

        var items : List<LocOfRestaurant>? = null
        realm.writeBlocking {
            items = realm.query<LocOfRestaurant>().find().toList()
        }
        return items!!

    }

    override fun updateRestaurant() {

    }

    override fun deleteRestaurant() {

    }

    override fun closeRealm() {
        realm.close()
    }
}