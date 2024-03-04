package com.android.aviro.data.datasource.restaurant

import android.util.Log
import com.android.aviro.data.api.RestaurantService
import com.android.aviro.data.model.restaurant.RestaurantDAO
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class RestaurantLocalDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService,
) : RestaurantLocalDataSource {

    lateinit var realm: Realm

    override fun saveRestaurant(RestaurantList : List<RestaurantDAO>) {
        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)

            realm.writeBlocking {
                Log.d("saveRestaurant", "start save")
                RestaurantList.forEach {
                    copyToRealm(RestaurantDAO().apply {
                        placeId = it.placeId
                        x = it.x
                        y = it.y
                        title = it.title
                        category = it.category
                        allVegan = it.allVegan
                        someMenuVegan = it.someMenuVegan
                        ifRequestVegan = it.ifRequestVegan

                    })
                }
            }

        closeRealm()

    }

    override fun getRestaurant() : List<RestaurantDAO> {
        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)

        var items : List<RestaurantDAO>? = null

            realm.writeBlocking {
                items = realm.query<RestaurantDAO>().find().toList()
            }

        Log.d("Realm","${items}")
        return items!!

    }

    override fun getRestaurantId() : List<String> {
        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm= Realm.open(config)

        var items = arrayListOf<RestaurantDAO>()
            realm.writeBlocking {
                items = realm.query<RestaurantDAO>().find().toList() as ArrayList<RestaurantDAO>
            }

        val id_list: List<String> = items.map { it.placeId }

        return id_list

    }

    override fun updateRestaurant(update_list : List<RestaurantDAO>) {
            val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
            realm = Realm.open(config)

            // 새로 생긴 데이터인지 기존의 데이터인지 확인하는 작업 필요
            realm.writeBlocking {
                update_list.map { locOfRestaruantItem ->
                    val id = locOfRestaruantItem.placeId
                    Log.d("Realm id","${id}")

                    val updatedRestaurant: RealmResults<RestaurantDAO> =
                        realm.query<RestaurantDAO>("placeId == '${id}'")
                            .find()

                    Log.d("Realm updatedRestaurant","${updatedRestaurant}")

                    if(updatedRestaurant.isNotEmpty()) {
                        updatedRestaurant.let {
                            updatedRestaurant[0].x = locOfRestaruantItem.x
                            updatedRestaurant[0].y = locOfRestaruantItem.y
                            updatedRestaurant[0].title = locOfRestaruantItem.title
                            updatedRestaurant[0].category = locOfRestaruantItem.category
                            updatedRestaurant[0].allVegan = locOfRestaruantItem.allVegan
                            updatedRestaurant[0].someMenuVegan = locOfRestaruantItem.someMenuVegan
                            updatedRestaurant[0].ifRequestVegan = locOfRestaruantItem.ifRequestVegan
                        }

                    } else {
                        // 없으면 생성해줘야 함
                        //saveRestaurant(listOf(locOfRestaruantItem))
                    }

                }
            }

            closeRealm()


    }

    override fun deleteRestaurant(delete_list : List<String>) {

        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)

            realm.writeBlocking {
                delete_list.forEach {

                    val deletedRestaurant =
                        realm.query<RestaurantDAO>("placeId == '${it}'")
                            .find().firstOrNull()

                    Log.d("Realm deletedRestaurant", "${deletedRestaurant}")

                    if (deletedRestaurant != null) {
                        //deletedRestaurant.apply {
                        //delete(deletedRestaurant)
                        findLatest(deletedRestaurant)
                            ?.also {
                                delete(it)
                            }
                        //delete<LocOfRestaurant>()
                        //delete<LocOfRestaurant>(deletedRestaurant)
                        //}
                    }

                }
            }

        closeRealm()

    }

    override fun closeRealm() {
        realm.close()
    }
}