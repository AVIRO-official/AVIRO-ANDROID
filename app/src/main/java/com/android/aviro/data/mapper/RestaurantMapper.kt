package com.android.aviro.data.mapper

import com.android.aviro.data.model.marker.MarkerDAO
import com.android.aviro.data.model.restaurant.*
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.domain.entity.marker.MarkerOfMap
import com.android.aviro.domain.entity.menu.Menu
import com.android.aviro.domain.entity.restaurant.BookMark
import com.android.aviro.domain.entity.restaurant.RestaurantMenu
import com.android.aviro.domain.entity.restaurant.RestaurantReview
import com.android.aviro.domain.entity.restaurant.RestaurantSummary
import com.android.aviro.domain.entity.review.Review
import com.android.aviro.domain.entity.search.SearchedRestaurant
import com.android.aviro.domain.entity.search.SearchedRestaurantList

/*
fun SearchedListFromKakaoResponse.toSearchedRestaurantList() : SearchedRestaurantList {

    val searched_restaurant_list = mutableListOf<SearchedRestaurant>()
    this.documents.map {
        val item = SearchedRestaurant(
            place_name = it.place_name,
            distance = it.distance,
            x = it.x,
            y = it.y,
            category_group_code = it.category_group_code,
            address_name = it.address_name,
            road_address_name = it.road_address_name,
            place_url = it.place_url,
            phone = it.phone
            )
        searched_restaurant_list.add(item)
    }

    return SearchedRestaurantList(
        is_end = this.meta.is_end,
        total_count = this.total_count,
        pageable_count = this.meta.pageable_count,
        restaurantList = searched_restaurant_list
    )

}

 */



fun List<MarkerDAO>.toMarker() : List<MarkerOfMap> {
    val marker_list = mutableListOf<MarkerOfMap>()
    this.map {
        val marker = MarkerOfMap(
            placeId = it.placeId,
          veganType = it.veganType,
         marker = it.marker
        )
        marker_list.add(marker)
    }

    return marker_list

}

fun BookmarkResponse.toBookMarker() : BookMark {
    return BookMark(
        bookmarks = this.bookmarks
    )
}

fun RestaurantSummaryResponse.toRestaurantSummary() : RestaurantSummary {
    return RestaurantSummary(
        placeId = this.placeId,
        title =  this.title,
        category = this.category,
        address = this.address,
        commentCount = this.commentCount
    )
}

fun RestaurantMenuResponse.toRestaurantMenu() : RestaurantMenu {
    val menu_list = mutableListOf<Menu>()
    this.menuArray.map{
        val review = Menu(
            menuId = it.menuId,
            menuType = it.menuType,
            menu = it.menu,
            price = it.price,
            howToRequest = it.howToRequest,
            isCheck = it.isCheck
        )
        menu_list.add(review)
    }

    return RestaurantMenu(
        count = this.count,
        updatedTime = this.updatedTime,
        menuArray = menu_list
    )
}

fun RestaurantReviewResponse.toRestaurantReview() : RestaurantReview {
    val comment_list = mutableListOf<Review>()
    this.commentArray.map{
        val review = Review(
            commentId = it.commentId,
             userId = it.userId,
            content = it.content,
            updatedTime = it.updatedTime,
            nickname = it.nickname,
        )
        comment_list.add(review)
    }

    return RestaurantReview(
        commentArray = comment_list
    )
}


