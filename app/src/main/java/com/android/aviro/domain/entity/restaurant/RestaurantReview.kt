package com.android.aviro.domain.entity.restaurant

import com.android.aviro.data.model.review.ReviewDTO
import com.android.aviro.domain.entity.review.Review

data class RestaurantReview(
    val commentArray: List<Review>
)
