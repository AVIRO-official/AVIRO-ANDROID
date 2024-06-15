package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.restaurant.BeforeAfterString
import com.aviro.android.domain.entity.restaurant.MenuUpdating
import com.aviro.android.domain.entity.restaurant.PhoneUpdating
import com.aviro.android.domain.entity.restaurant.TimetableUpdating
import com.aviro.android.domain.entity.restaurant.UrlUpdating
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject

class UpdateRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository,
    private val getMyInfoUseCase: GetMyInfoUseCase
){
    suspend fun updatePhone(placeId : String, placeName : String, before : String, after : String) : MappingResult {
         val userId = getMyInfoUseCase.getUserId()
        val nickName = getMyInfoUseCase.getNickname()
        userId.let { user_id ->
            when(user_id) {
                is MappingResult.Success<*> -> {
                    nickName.let {nickname ->
                        when(nickname) {
                            is MappingResult.Success<*> -> return restaurantRepository.updatePhone(
                                PhoneUpdating(
                                    placeId,
                                    placeName,
                                    user_id.data as String,
                                    nickname.data as String,
                                    BeforeAfterString(before, after)
                                )
                            )
                            is MappingResult.Error -> return nickname
                        }
                    }

                }
                is MappingResult.Error -> return userId
            }
        }

    }
    suspend fun updateUrl(placeId : String, placeName : String, before : String, after : String) : MappingResult{

        val userId = getMyInfoUseCase.getUserId()
        val nickName = getMyInfoUseCase.getNickname()
        userId.let { user_id ->
            when(user_id) {
                is MappingResult.Success<*> -> {
                    nickName.let {nickname ->
                        when(nickname) {
                            is MappingResult.Success<*> -> return restaurantRepository.updateUrl(
                                UrlUpdating(
                                    placeId,
                                    placeName,
                                    user_id.data as String,
                                    nickname.data as String,
                                    BeforeAfterString(before, after)
                                )
                            )
                            is MappingResult.Error -> return nickname
                        }
                    }
                }
                is MappingResult.Error -> return userId
            }
        }

    }

    suspend fun updateTime(placeId : String, timetable : TimetableUpdating) : MappingResult{ //UpdatingTimetableEntity (안됨)
        val userId = getMyInfoUseCase.getUserId()
        userId.let {
            when(it) {
                is MappingResult.Success<*> -> {
                    return restaurantRepository.updateTimetable(placeId, it.data as String, timetable)
                }
                is MappingResult.Error -> return userId
            }
        }
    }

    suspend fun updateMenu(updating : MenuUpdating) : MappingResult{
        val userId = getMyInfoUseCase.getUserId()
        userId.let {user_id ->
            when(user_id) {
                is MappingResult.Success<*> -> {
                    return restaurantRepository.updateMenus(user_id.data as String, updating)
                }
                is MappingResult.Error -> return userId
            }
        }
    }

    suspend fun updateRestaurantInfo(updating : MutableMap<String, Any>) : MappingResult { //RestaurantInfoUpdating
        val userId = getMyInfoUseCase.getUserId()
        val nickName = getMyInfoUseCase.getNickname()
        userId.let {
            when(userId) {
                is MappingResult.Success<*> -> {
                    nickName.let {
                        when(nickName) {
                            is MappingResult.Success<*> -> {
                                updating["nickname"] = nickName.data as String
                                updating["userId"] = userId.data as String
                                return restaurantRepository.updateRestaurantInfo(updating)
                            }
                            is MappingResult.Error -> return nickName
                        }
                    }
                }
                is MappingResult.Error -> return userId
            }
        }
    }


}