package com.aviro.android.data.model.restaurant

import com.aviro.android.data.model.review.ReviewDTO


data class RestaurantReviewResponse (
        val commentArray: List<ReviewDTO>

        )