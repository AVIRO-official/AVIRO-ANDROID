package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchedPlaceListResponse (
        @SerializedName("id")
        val id : String,
        @SerializedName("meta")
        val meta : Meta,
        @SerializedName("total_count")
        val total_count : Int,
        @SerializedName("documents")
        val documents : List<Document>,
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

data class Document(
        @SerializedName("id")
        val id : String,
        @SerializedName("place_name")
        val place_name : String,
        @SerializedName("distance")
        val distance : String,
        @SerializedName("x")
        val x : String,
        @SerializedName("y")
        val y : String,

        @SerializedName("category_name")
        val category_name : String,
        @SerializedName("category_group_code")
        val category_group_code : String,
        @SerializedName("category_group_name")
        val category_group_name : String,

        @SerializedName("address_name")
        val address_name : String,
        @SerializedName("road_address_name")
        val road_address_name : String,

        @SerializedName("place_url")
        val place_url : String,
        @SerializedName("phone")
        val phone : String
)

