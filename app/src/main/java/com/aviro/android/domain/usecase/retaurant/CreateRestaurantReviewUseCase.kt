package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.review.ReviewAdding
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject

class CreateRestaurantReviewUseCase @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    private val restaurantRepository : RestaurantRepository
) {

    suspend fun invoke(commentId : String, placeId : String, content : String) : MappingResult {
        val userId = getMyInfoUseCase.getUserId()
        when(userId) {
            is MappingResult.Success<*> -> {
                return restaurantRepository.createRestaurantReview(ReviewAdding(commentId, placeId, userId.data.toString(), content))
            }
            is MappingResult.Error -> {
                return userId
            }
        }

    }

}