package com.aviro.android.data.datasource.marker

import android.util.Log
import android.util.LruCache
import com.aviro.android.common.getMarkerIcon
import com.aviro.android.common.getVeganType
import com.aviro.android.data.datasource.restaurant.RestaurantLocalDataSource
import com.aviro.android.data.model.marker.MarkerDAO
import com.aviro.android.data.model.restaurant.RestaurantDAO
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
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
        var veganTypeColor = ""


        // 마커 아이콘 설정
        marker.icon = getMarkerIcon(updated_marker.category, updated_marker.allVegan, updated_marker.someMenuVegan, updated_marker.ifRequestVegan)
        marker.captionText = updated_marker.title
        marker.captionRequestedWidth = 15
        marker.captionOffset = 1 // 마커와 텍스트 간격
        marker.captionTextSize = 9f
        marker.captionMinZoom = 11.0
        marker.captionMaxZoom = 18.0


        veganTypeColor = getVeganType(updated_marker.allVegan, updated_marker.someMenuVegan, updated_marker.ifRequestVegan)

        val newMarker = MarkerDAO(updated_marker.placeId, updated_marker.title, updated_marker.category, veganTypeColor, updated_marker.allVegan, updated_marker.someMenuVegan, updated_marker.ifRequestVegan, updated_marker.y, updated_marker.x, marker)
        memoryCache.put(updated_marker.placeId, newMarker)

        return newMarker
    }



    override fun updateMarker(marker_id : List<RestaurantDAO>) : List<MarkerDAO> {

        val new_marker_list = arrayListOf<MarkerDAO>()

        marker_id.map {

            val markerEntity = memoryCache.get(it.placeId)

            if(markerEntity == null) {   // 업데이트 하려는 마커가 없음
                new_marker_list.add(createMarker(it)) // 마커 새로 생성
            } else {
               // CoroutineScope(Dispatchers.Main).launch {
                    //Marker(LatLng(updated_marker.y, updated_marker.x))
                    markerEntity.marker.position = LatLng(it.y, it.x)

                    markerEntity.marker.icon = getMarkerIcon(it.category, it.allVegan, it.someMenuVegan, it.ifRequestVegan)
                    markerEntity.category = it.category
                    markerEntity.title = it.title
                    markerEntity.veganTypeColor = getVeganType(it.allVegan, it.someMenuVegan, it.ifRequestVegan)
                    markerEntity.allVegan = it.allVegan
                    markerEntity.someMenuVegan = it.someMenuVegan
                    markerEntity.ifRequestVegan = it.ifRequestVegan

                    // 참조되어 있으면 굳이 필요없을지도...?
                    memoryCache.put(it.placeId, markerEntity)
                        //}.join()
                new_marker_list.add(markerEntity)

            }
        }


        return new_marker_list

    }

    // 화면에 표시하고 싶은 마커만 반환
    override fun getAllMarker() : List<MarkerDAO> {
        val custom_marker_list = arrayListOf<MarkerDAO>()
        val allKeys = memoryCache.snapshot().keys


        for (key in allKeys) {
            custom_marker_list.add(memoryCache.get(key))
        }

        return custom_marker_list

    }

    override fun getMarker(key_list : List<String>) : List<MarkerDAO> {
        val custom_marker_list = arrayListOf<MarkerDAO>()

        key_list.map { key ->
            custom_marker_list.add(memoryCache.get(key))
        }

        return custom_marker_list
    }

    override fun deleteMarker(marker_id : List<String>) {
        marker_id.map {
            val markerEntity = memoryCache.get(it)

            if(markerEntity != null) {
                Log.d("deleteMarker","${markerEntity}")
                // main thread에서만 가능
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("deleteMarker","Lopper 실행")
                    markerEntity.marker.map = null
                }

                memoryCache.remove(it)

            }

        }

    }

}