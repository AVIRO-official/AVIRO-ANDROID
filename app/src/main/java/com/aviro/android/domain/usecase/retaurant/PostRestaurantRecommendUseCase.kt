package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject

class PostRestaurantRecommendUseCase  @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    private val restaurantRepository : RestaurantRepository
    ) {
    suspend operator fun invoke(placeId : String, isRecommend : Boolean) : MappingResult {

        val userId = getMyInfoUseCase.getUserId()
        userId.let { user_id ->
            when (user_id) {
                is MappingResult.Success<*> -> {
                    restaurantRepository.recommendRestaurant(placeId, user_id.data as String, isRecommend)
                    return restaurantRepository.getBookmarkRestaurant(user_id.data as String)
                }

                is MappingResult.Error -> {
                    return userId
                }
            }
        }

    }

}