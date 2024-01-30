package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchRestaurantResponse (
        @SerializedName("id")
        val id : String,
        @SerializedName("meta")
        val meta : Meta,
        @SerializedName("total_count")
        val total_count : Int,
        @SerializedName("documents")
        val documents : List<SearchEntity>,
)

data class Meta(
        @SerializedName("is_end")
        val is_end : Boolean,
        @SerializedName("pageable_count")
        val pageable_count : Int,
        @SerializedName("same_name")
        val same_name : SameName,
)
data class SameName(
        @SerializedName("keyword")
        val keyword : String,
        @SerializedName("region")
        val region : List<String>,
        @SerializedName("selected_region")
        val selected_region : String,
)

