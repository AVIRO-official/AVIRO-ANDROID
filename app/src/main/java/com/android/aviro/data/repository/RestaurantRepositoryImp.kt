package com.android.aviro.data.repository

import android.util.Log
import com.android.aviro.data.datasource.marker.MarkerMemoryCacheDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantAviroDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantKakaoDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSource
import com.android.aviro.data.mapper.toBookMarker
import com.android.aviro.data.mapper.toMarker
import com.android.aviro.data.mapper.toRestaurantSummary
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.marker.MarkerDAO
import com.android.aviro.data.model.restaurant.*
import com.android.aviro.data.model.search.RequestedVeganTypeOfRestaurantDTO
import com.android.aviro.data.model.search.RestaurantVeganTypeRequest
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.domain.entity.VeganOptions
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.search.SearchedRestaurantList
import com.android.aviro.domain.repository.RestaurantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class RestaurantRepositoryImp @Inject constructor (
    private val restaurantAviroDataSource: RestaurantAviroDataSource,
    private val restaurantLocalDataSource: RestaurantLocalDataSource,
    private val restaurantKakaoDataSource: RestaurantKakaoDataSource,
    private val markerCacheDataSource: MarkerMemoryCacheDataSource
) : RestaurantRepository {


    // 어비로 서버로부터 데이터 불러와서 업데이트
    override suspend fun getRestaurantLoc(isInitMap : Boolean, x : String, y : String, wide : String, time : String) : MappingResult { //List<MarkerEntity>?

        var response :Result<DataResponse<ReataurantListReponse>>

        val realmData = restaurantLocalDataSource.getRestaurant()
        val isRealmEmpty = realmData.isEmpty()
        restaurantLocalDataSource.closeRealm()

        lateinit var result : MappingResult

        if(isRealmEmpty) {
            response =  restaurantAviroDataSource.getRestaurant(request = ReataurantListRequest(x, y, wide, time))
        }else {
            // 마커를 모두 다 가져와야 하는 경우와 그렇지 않은 경우로 나뉨
            val current_time = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current_time.format(formatter)
            response =  restaurantAviroDataSource.getRestaurant(request = ReataurantListRequest(x, y, wide, formatted))
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

                    }

                    if(data.deletedPlace != null){
                        Log.d("가게데이터","가게 삭제 있음")
                        restaurantLocalDataSource.deleteRestaurant(data.deletedPlace)  // 삭제된 위치 데이터 Realm 반영
                        markerCacheDataSource.deleteMarker(data.deletedPlace) // 캐시 반영
                    }
                }

                // 마커 가져옴
                val new_marker_list = getMarker(isInitMap, it.data)
                // 가져온 마커가 있을때 맵핑
                if(new_marker_list != null) {
                    result =  MappingResult.Success(it.message, new_marker_list.toMarker())
                } else {
                    result =  MappingResult.Success(it.message,null)
                }


            } else {
                // ERORR
                when(code){
                    400 -> result = MappingResult.Error(it.message ?: "잘못된 요청값으로 인해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
                    500 -> result = MappingResult.Error(it.message ?: "서버 오류가 발생해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.message ?: "알 수 없는 오류가 발생해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
                }
            }

        }.onFailure {
            result =  MappingResult.Error(it.message ?: "알 수 없는 오류가 발생해 가게 데이터를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.")
        }
       return result

    }

    // 업데이트 이후 필요한 마커데이터만 가져옴 (화면에 새로 그려줄 마커)
    override fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantListReponse) : List<MarkerDAO>? {
        var new_marker_list : List<MarkerDAO>? = null
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


  /*  // 마커 데이터만 필요한 마커
    override fun getMarkerForBookmark() : List<MarkerOfMap>? { //bookmark_list : List<String>
        return markerCacheDataSource.getAllMarker()?.toMarker()
    }*/


    // 키워드로 가게를 검색함
    override suspend fun searchRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : MappingResult {
        lateinit var result: MappingResult
      
            val response =
                restaurantKakaoDataSource.getSearchedRestaurant(keyword, x, y, page, size, sort)

            response.onSuccess {
                // 맵핑
                if (it.documents.size != 0) {
                    // 검색 페이지 end 여부
                    val isEnd = it.meta.is_end
                    // 어비로 서버에 비건 정보 받아옴
                    /* suspend 함수는 병렬적으로 수행되기 때문에 이 함수 실행이 완료될 때까지 기다려주는 것이 아니라
                     * 병렬적으로 처리해버린다. 비건 정보를 받아와 최종 검색 결과를 반환할 때까지 기다려줌 */

                    val job = CoroutineScope(Dispatchers.IO).launch {
                        result = getVeganTypeOfSearching(isEnd, it.documents)
                    }
                    job.join()

                } else {
                    result = MappingResult.Error("검색 결과가 없어요")
                }

            }.onFailure {
                result = MappingResult.Error("검색 결과가 없어요")
            }

        return result

    }

    // 검색한 가게들의 비건 유형을 받아옴
    override suspend fun getVeganTypeOfSearching(isEnd : Boolean, SearchedPlaceRawList : List<Document>) : MappingResult { //Result<List<SearchedRestaurantItem>>
        val request_list_veganTypeOfRestaurant = mutableListOf<RequestedVeganTypeOfRestaurantDTO>()
        val item_list  = mutableListOf<SearchedRestaurantItem>()

        SearchedPlaceRawList.map {
            if(it.category_group_code == "CE7" || it.category_group_code == "FD6" || it.category_group_code == "SW8" ||
                it.category_group_code == "AT4" || it.category_group_code == "PO3") {
                request_list_veganTypeOfRestaurant.add(RequestedVeganTypeOfRestaurantDTO(it.place_name, it.x.toDouble(), it.y.toDouble()))
                item_list.add(SearchedRestaurantItem(null, it.place_name, it.road_address_name, it.distance, it.x, it.y, VeganOptions(false, false, false)))
            }
        }

        val request = RestaurantVeganTypeRequest(request_list_veganTypeOfRestaurant)

        lateinit var result : MappingResult
        val respose = restaurantAviroDataSource.getVeganTypeOfSearching(request)
        respose.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                // 등록되어 있는 가게만 반환
                data.placeList.map { veganType ->
                    if (veganType.index in item_list.indices) {
                        item_list[veganType.index!!].placeId = veganType.placeId
                        item_list[veganType.index].veganType.allVegan = veganType.allVegan
                        item_list[veganType.index].veganType.someMenuVegan = veganType.someMenuVegan
                        item_list[veganType.index].veganType.ifRequestVegan = veganType.ifRequestVegan
                    }
                }
               
                val searchedRestaurantList = SearchedRestaurantList(isEnd, item_list)
                result = MappingResult.Success(null, searchedRestaurantList)

            } else {
                // 비건 정보 가져오기 실패
                // 그냥 검색 결과만 화면에 띄움 -> 클릭해도 placeId 없기 떄문에 상세 정보 제공 X, 좌표 이동만 가능
                val searchedRestaurantList = SearchedRestaurantList(isEnd, item_list)
                result = MappingResult.Success("알 수 없는 오류로 비건 식당 정보를 제공하지 못합니다.\n다시 시도해주시면 더 자세한 비건 식당 정보를 알 수 있어요!", searchedRestaurantList)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result

    }

    override suspend fun getBookmarkRestaurant(userId : String) : MappingResult {
        val response = restaurantAviroDataSource.getBookmarkRestaurant(userId)
        lateinit var  result : MappingResult

        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                result =  MappingResult.Success(null, data.bookmarks)
            } else {
                result =  MappingResult.Error("알 수 없는 에러가 발생했습니다.")
            }

        }.onFailure {
            result =  MappingResult.Error(it.message)
        }

        return result

    }

    // 선택한 가게의 상세 정보를 불러옴
    override suspend fun getRestaurantSummary(placeId : String) : MappingResult {
        val response =  restaurantAviroDataSource.getRestaurantSummary(placeId)

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                result =  MappingResult.Success(it.message, it.data.toRestaurantSummary())
            } else {
                result =  MappingResult.Error(it.message)
            }

        }.onFailure {
            result =  MappingResult.Error(it.message)
        }

        return result
    }


    }
