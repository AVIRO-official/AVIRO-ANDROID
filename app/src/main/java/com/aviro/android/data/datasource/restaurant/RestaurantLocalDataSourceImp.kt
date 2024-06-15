package com.aviro.android.data.datasource.restaurant

import android.util.Log
import com.aviro.android.data.api.RestaurantService
import com.aviro.android.data.model.restaurant.RestaurantDAO
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import javax.inject.Inject


class RestaurantLocalDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService,
) : RestaurantLocalDataSource {

    lateinit var realm: Realm

     override suspend fun saveRestaurant(RestaurantList : List<RestaurantDAO>) {
        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)
        CoroutineScope(Dispatchers.IO).launch {
            realm.writeBlocking {
                RestaurantList.forEach {
                    val existingRestaurant = this.query<RestaurantDAO>("placeId == '${it.placeId}'").find().firstOrNull()
                    Log.d("saveRestaurant","${existingRestaurant}")
                    if(existingRestaurant != null) {

                    } else {
                        Log.d("saveRestaurant","저장")
                        copyToRealm(RestaurantDAO().apply {//copyToRealm
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
            }
       }.join()

        //closeRealm()

    }
    suspend override fun getRestaurant() : List<RestaurantDAO> {
        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)

        var items : List<RestaurantDAO>? = null


        // 오래 걸릴 수 있는 작업이므로 main이 아닌 다른 스레드에서 작업
        CoroutineScope(Dispatchers.IO).launch {
            realm.writeBlocking { // Realm 스레드를 block 하여 작업중 다른 접근 막음

                items = realm.query<RestaurantDAO>().find().toList()
                Log.d("getRestaurant:RestaurantLocalDataSourceImp","${items}")
            }
        }.join()

       // 작업이 완료될때까지 기다린다 (하지만 블록한 스레드가 메인이 아니기 때문에 메인 스레드에 영향 x)


        return items!!

    }


     override suspend fun getRestaurantId() : List<String> {
        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)

        //var items = arrayListOf<RestaurantDAO>()
        var items : List<RestaurantDAO>? = null
        CoroutineScope(Dispatchers.IO).launch {
            realm.writeBlocking {
                sleep(6000)
                items = realm.query<RestaurantDAO>().find().toList() as ArrayList<RestaurantDAO>
            }
        }.join()
        val id_list: List<String> = items!!.map { it.placeId }

        return id_list
    }

    override suspend fun updateRestaurant(update_list : List<RestaurantDAO>) {
            val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
            realm = Realm.open(config)

            // 새로 생긴 데이터인지 기존의 데이터인지 확인하는 작업 필요
        CoroutineScope(Dispatchers.IO).launch {
            realm.writeBlocking {

                update_list.map { locOfRestaruantItem ->
                    val id = locOfRestaruantItem.placeId

                    val updatedRestaurant: RealmResults<RestaurantDAO> =
                        this.query<RestaurantDAO>("placeId == '${id}'")
                            .find()

                    Log.d("RestaurantLocalDataSourceImp", "updateRestaurant")
                    if (updatedRestaurant.isNotEmpty()) {
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
                        CoroutineScope(Dispatchers.IO).launch {
                            saveRestaurant(listOf(locOfRestaruantItem))
                        }
                    }

                }
            }
        }.join()

            //closeRealm()


    }

     suspend override fun deleteRestaurant(delete_list : List<String>) {

        val config = RealmConfiguration.create(schema = setOf(RestaurantDAO::class))
        realm = Realm.open(config)

        CoroutineScope(Dispatchers.IO).launch {
            realm.writeBlocking {
                Log.d("RestaurantLocalDataSourceImp", "deleteRestaurant")
                delete_list.forEach {

                    val deletedRestaurant =
                        realm.query<RestaurantDAO>("placeId == '${it}'")
                            .find().firstOrNull()

                    if (deletedRestaurant != null) {
                        findLatest(deletedRestaurant)
                            ?.also {
                                delete(it)
                            }
                        //delete<LocOfRestaurant>()
                        //delete<LocOfRestaurant>(deletedRestaurant)
                        //}
                    }
                    /*
                    val deletedRestaurant =
                        realm.query<RestaurantDAO>("placeId == '${it}'")
                            .first()
                            .find()
                            ?.also {
                                delete(it)
                            }
                     */

                }
            }
        }.join()

        //closeRealm()

    }

    override fun closeRealm() {
        realm.close()
    }
}