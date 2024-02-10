package com.android.aviro.data.entity.restaurant

import com.android.aviro.domain.entity.ReviewEntity
import com.google.gson.annotations.SerializedName

data class RestaurantReview (

        @SerializedName("commentArray")
        val commentArray: List<ReviewEntity>

        )