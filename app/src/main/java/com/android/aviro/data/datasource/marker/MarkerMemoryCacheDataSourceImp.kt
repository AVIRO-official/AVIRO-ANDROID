package com.android.aviro.data.datasource.marker

import android.util.Log
import android.util.LruCache
import com.android.aviro.R
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSource
import com.android.aviro.data.model.marker.MarkerDAO
import com.android.aviro.data.model.restaurant.RestaurantDAO
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MarkerMemoryCacheDataSourceImp @Inject constructor(
    private val restaurantLocalDataSource : RestaurantLocalDataSource
): MarkerMemoryCacheDataSource {

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8 // 최대 메모리 크기의 1/8을 사용
    private val memoryCache = LruCache<String, MarkerDAO>(cacheSize)

    /*
    * 마커 데이터
    * 1. 마커데이터 == 0 일 경우 새로 만들기
    * 2. 마커 데이터 모두 반환하기
    * 3. 마커 데이터 업데이트하기 -> 새롭게 생기는 데이터만 반환하기
    * 4. 마커 데이터 제거하기
    * 5. 마커 필터링 하기 (즐찾, 카테고리) -> 선택된 애들만 남기고 필요하면 이미지 바꿔주기, 아닌 애들은 null 처리 해주기 (다시 그려줄때마다 반환)
    */


    override fun getSize() : Int {
        return memoryCache.size()
    }

    override fun createMarker(updated_marker : RestaurantDAO) : MarkerDAO {
        val marker = Marker(LatLng(updated_marker.y, updated_marker.x))
        var veganType = ""

        if (updated_marker.allVegan) {
            marker.icon = OverlayImage.fromResource(R.drawable.marker_default_green)
            veganType = "green"
        } else if (updated_marker.someMenuVegan) {
            marker.icon =
                OverlayImage.fromResource(R.drawable.marker_default_orange)
            veganType = "orange"
        } else {
            marker.icon =
                OverlayImage.fromResource(R.drawable.marker_default_yellow)
            veganType = "yellow"
        }

        val newMarker = MarkerDAO(updated_marker.placeId, veganType, marker)
        memoryCache.put(updated_marker.placeId, newMarker)

        return newMarker // 생성한 마커 반환
    }


    // 업데이트 된 아이들을 다시 그릴 필요가 있는지가 궁금함
    override fun updateMarker(marker_id : List<RestaurantDAO>) : List<MarkerDAO> {
        Log.d("updated_marker_id","${marker_id}")

        val new_marker_list = arrayListOf<MarkerDAO>()

        marker_id.map {
            val markerEntity = memoryCache.get(it.placeId)

            if(markerEntity == null) {   // 업데이트 하려는 마커가 없음
                new_marker_list.add(createMarker(it)) // 마커 새로 생성
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    markerEntity.marker.position = LatLng(it.y, it.x)

                    if (it.allVegan) {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_green)
                        markerEntity.veganType = "green"
                    } else if (it.someMenuVegan) {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_orange)
                        markerEntity.veganType = "orange"
                    } else {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_yellow)
                        markerEntity.veganType = "yellow"
                    }

                    // 참조되어 있으면 굳이 필요없을지도...?
                    memoryCache.put(it.placeId, markerEntity)
                }
                /*
                Handler(Looper.getMainLooper()).post {
                    markerEntity.marker.position = LatLng(it.y, it.x)

                    if (it.allVegan) {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_green)
                        markerEntity.veganType = "green"
                    } else if (it.someMenuVegan) {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_orange)
                        markerEntity.veganType = "orange"
                    } else {
                        markerEntity.marker.icon =
                            OverlayImage.fromResource(R.drawable.marker_default_yellow)
                        markerEntity.veganType = "yellow"
                    }

                    memoryCache.put(it.placeId, markerEntity)


                }

                 */
            }

        }
        return new_marker_list

    }

    // 화면에 표시하고 싶은 마커만 반환
    override fun getAllMarker() : List<MarkerDAO> { // 그냥 모든 마커 다 불러옴
        val custom_marker_list = arrayListOf<MarkerDAO>()
        val allKeys = memoryCache.snapshot().keys

        // 각 키에 대해 데이터 가져오기
        for (key in allKeys) {
            custom_marker_list.add(memoryCache.get(key))
        }

        return custom_marker_list

    }

    override fun getMarker(key_list : List<String>) : List<MarkerDAO> {
        val custom_marker_list = arrayListOf<MarkerDAO>()
        // key에 대응하는 마커가 없는 경우 -> 일관성 무너진 것
        key_list.map { key ->
            custom_marker_list.add(memoryCache.get(key))
        }

        return custom_marker_list
    }

    override fun deleteMarker(marker_id : List<String>) {
        marker_id.map {
            val markerEntity = memoryCache.get(it) //"4D51A05F-1A90-490D-85CF-9EA9D3B44464"

            if(markerEntity != null) {
                Log.d("deleteMarker","${markerEntity}")
                // main thread에서만 가능
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("deleteMarker","Lopper 실행")
                    markerEntity.marker.map = null
                }
                // 마커에 표시된 지도 객체 없애주기
                /*
                Handler(Looper.getMainLooper()).post {
                    Log.d("deleteMarker","Lopper 실행")
                    markerEntity.marker.map = null
                }
                 */

                memoryCache.remove(it)

            }

        }

    }


}