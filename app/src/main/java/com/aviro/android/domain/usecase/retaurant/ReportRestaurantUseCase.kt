package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.restaurant.ReportRestaurant
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject

class ReportRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase
){
    suspend fun invoke(placeId : String, reportCode : Int) : MappingResult {
        val userId = getMyInfoUseCase.getUserId()
        val nickname = getMyInfoUseCase.getNickname()
        when(userId) {
            is MappingResult.Success<*> -> {
                val user_id = userId.data.toString()
                when(nickname) {
                    is MappingResult.Success<*> -> {
                        return restaurantRepository.reportRestaurant(ReportRestaurant(placeId, user_id, nickname.data.toString(), reportCode ))
                    }
                    is MappingResult.Error -> {
                        return nickname
                    }
                    }
                }
            is MappingResult.Error -> {
                return userId
            }
        }

    }
}