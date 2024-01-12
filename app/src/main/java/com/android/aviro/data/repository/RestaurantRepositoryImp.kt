package com.android.aviro.data.repository

import android.util.Log
import com.android.aviro.R
import com.android.aviro.data.datasource.marker.MarkerCacheDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSource
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import com.android.aviro.domain.repository.RestaurantRepository
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import javax.inject.Inject

class RestaurantRepositoryImp @Inject constructor (
    private val restaurantDataSource: RestaurantDataSource,
    private val restaurantLocalDataSource: RestaurantLocalDataSource,
    private val markerCacheDataSource: MarkerCacheDataSource
) : RestaurantRepository {

    override suspend fun getRestaurant(request: RestaurantRequestDTO){

        val CustomMarkerList = arrayListOf<MarkerEntity>()

        // Local DB 초기화
        if (!restaurantLocalDataSource.getRestaurant().isEmpty()) {
            //Log.d("restaurantList", "${restaurantList}")
            // Remote에서 가게 데이터 가져옴
            val response = restaurantDataSource.getRestaurant(request)

            response.onSuccess {
                //restaurantLocalDataSource.saveRestaurant(it.updatedPlace)

                // Maker 생성
                val restaurantList = restaurantLocalDataSource.getRestaurant()
                Log.d("restaurantList", "${restaurantList}")
                restaurantList.map {

                    var veganType = ""

                    val marker = Marker()
                    marker.position = LatLng(it.y, it.x)

                    if (it.allVegan) {
                        marker.icon = OverlayImage.fromResource(R.drawable.marker_default_green)
                        veganType = "green"
                    } else if (it.someMenuVegan) {
                        marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_orange)
                        veganType = "orange"
                    } else {
                        marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_yellow)
                        veganType = "yellow"
                    }

                    val customMarker = MarkerEntity(it.placeId, veganType, marker)
                    CustomMarkerList.add(customMarker)
                }

                restaurantLocalDataSource.closeRealm()
                markerCacheDataSource.saveMarker(CustomMarkerList)
            }
        }
            //return CustomMarkerList
        }

    }
