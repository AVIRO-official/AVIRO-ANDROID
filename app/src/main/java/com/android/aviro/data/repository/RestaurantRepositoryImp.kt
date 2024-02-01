package com.android.aviro.data.repository

import android.util.Log
import com.android.aviro.data.datasource.marker.MarkerMemoryCacheDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantAviroDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSource
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.restaurant.*
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.domain.entity.VeganOptions
import com.android.aviro.domain.repository.RestaurantRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class RestaurantRepositoryImp @Inject constructor (
    private val restaurantAviroDataSource: RestaurantAviroDataSource,
    private val restaurantLocalDataSource: RestaurantLocalDataSource,
    private val markerCacheDataSource: MarkerMemoryCacheDataSource
) : RestaurantRepository {


    // 어비로 서버로부터 데이터 불러와서 업데이트
    override suspend fun getRestaurantLoc(x : String, y : String, wide : String, time : String, isInitMap : Boolean) : MappingResult { //List<MarkerEntity>?

        var response :Result<DataBodyResponse<ReataurantReponseDTO>>

        val realmData = restaurantLocalDataSource.getRestaurant()
        val isRealmEmpty = realmData.isEmpty()
        restaurantLocalDataSource.closeRealm()

        lateinit var  result : MappingResult

        if(isRealmEmpty) {
            response =  restaurantAviroDataSource.getRestaurant(request = RestaurantRequestDTO(x, y, wide, time))
        }else {
            // 마커를 모두 다 가져와야 하는 경우와 그렇지 않은 경우로 나뉨
            val current_time = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current_time.format(formatter)
            response =  restaurantAviroDataSource.getRestaurant(request = RestaurantRequestDTO(x, y, wide, formatted))
        }

        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if (code == 200 && data != null) {
                // SUCCESS
                // 데이터베이스 갱신 처리
                if(isRealmEmpty){
                    Log.d("가게데이터","첫 가게 데이터 초기화")
                    restaurantLocalDataSource.saveRestaurant(data.updatedPlace!!) // Realm 초기화
                    markerCacheDataSource.updateMarker(data.updatedPlace)
                } else {

                    if(markerCacheDataSource.getSize() == 0) {
                        Log.d("가게데이터","첫 실행 이후 마커 데이터가 없어서 생성")
                        val realmData = restaurantLocalDataSource.getRestaurant()
                        realmData.map {
                            markerCacheDataSource.createMarker(it)
                        }
                    }

                    if(data.updatedPlace != null) {
                        Log.d("가게데이터","업데이트 있음")
                        restaurantLocalDataSource.updateRestaurant(data.updatedPlace)  // 업데이트 된 위치 데이터 Realm 반영
                        // 업데이트 된 데이터 마커 객체에서 삭제하고 다시 생성
                        //new_marker_list = markerCacheDataSource.updateMarker(data.updatedPlace) // 새로 생긴 데이터면 반환
                    }

                    if(data.deletedPlace != null){
                        Log.d("가게데이터","가게 삭제 있음")
                        restaurantLocalDataSource.deleteRestaurant(data.deletedPlace)  // 삭제된 위치 데이터 Realm 반영
                        markerCacheDataSource.deleteMarker(data.deletedPlace) // 캐시 반영
                    }

                }
                result =  MappingResult.Success(it.statusCode, it.message, it.data)

            } else {
                // ERORR
                when(code){
                    400 -> result = MappingResult.Error(it.statusCode, it.message ?: "잘못된 요청값으로 인해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
                    500 -> result = MappingResult.Error(it.statusCode, it.message ?: "서버 오류가 발생해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.statusCode, it.message ?: "알 수 없는 오류가 발생해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
                }
            }

        }.onFailure {
            // ERORR
            result =  MappingResult.Error(0, it.message ?: "알 수 없는 오류가 발생해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
        }
       return result

        }

    // 업데이트 이후 필요한 마커데이터만 가져옴
    override fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantReponseDTO) : List<MarkerEntity>? {
        var new_marker_list : List<MarkerEntity>? = null
        if(isInitMap) {
            Log.d("가게데이터","모든 마커 그려줌")
            new_marker_list = markerCacheDataSource.getAllMarker()
        } else {
            Log.d("가게데이터","업데이트된 마커 그려줌")
            new_marker_list =
                reataurant_list.updatedPlace?.let { markerCacheDataSource.updateMarker(it) } // 새로 생긴 데이터면 반환
        }
        return new_marker_list
    }



    // 키워드로 가게를 검색함
    override suspend fun searchRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) :Result<SearchedPlaceListResponse> {
        return restaurantAviroDataSource.getSearchedRestaurant(keyword, x, y, page, size, sort)

    }

    // 검색한 가게들의 비건 유형을 받아옴
    override suspend fun getVeganTypeOfSearching(SearchedPlaceRawList : List<Document>) : Result<List<SearchedRestaurantItem>> {
        val placeArray = mutableListOf<PlaceInfo>()
        val result = mutableListOf<SearchedRestaurantItem>()

        SearchedPlaceRawList.map {
            val info = PlaceInfo(it.place_name, it.x.toDouble(), it.y.toDouble())
            placeArray.add(info)

            // SearchedRestaurantItem 형태로 바꿔줌
            result.add(SearchedRestaurantItem(null, it.place_name, it.road_address_name, it.distance, it.x, it.y, VeganOptions(false, false, false)))
        }

        val request = VeganOfSearchingRequest(placeArray)
        restaurantAviroDataSource.getVeganTypeOfSearching(request).onSuccess {
            if(it.statusCode == 200) {
                // 등록되어 있는 가게만 반환
                it.body.map {
                    if (it.index in result.indices) {
                        result[it.index].placeId = it.placeId
                        result[it.index].veganType.allVegan = it.allVegan
                        result[it.index].veganType.someMenuVegan = it.someMenuVegan
                        result[it.index].veganType.ifRequestVegan = it.ifRequestVegan
                    }
                }
            }
        }
        return Result.success(result)

    }

    // 선택한 가게의 상세 정보를 불러옴
    override suspend fun getRestaurantDetail() {


    }




    }
