package com.aviro.android.data.mapper

import com.aviro.android.data.model.kakao.address.AddressOfCoordFromKakaoResponse
import com.aviro.android.data.model.kakao.coordi.CoordOfAddressFromKakaoResponse
import com.aviro.android.data.model.marker.MarkerDAO
import com.aviro.android.data.model.menu.MenuDTO
import com.aviro.android.data.model.restaurant.*
import com.aviro.android.data.model.review.ReportReviewRequest
import com.aviro.android.data.model.search.PublicAddressListResponse
import com.aviro.android.domain.entity.marker.MarkerOfMap
import com.aviro.android.domain.entity.menu.Menu
import com.aviro.android.domain.entity.restaurant.*
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.domain.entity.review.ReviewAdding
import com.aviro.android.domain.entity.review.ReviewReporting
import com.aviro.android.domain.entity.search.CoordiOfPublicAddress
import com.aviro.android.domain.entity.search.PublicAddressItem
import com.aviro.android.domain.entity.search.PublicAddressList
import com.aviro.android.domain.entity.search.RoadAddressOfCoordi

fun Restaurant.toRestaurantRequest() : RestaurantRequest {
    val menuDTOList = mutableListOf<MenuDTO>()
    this.menuArray.map {
        val menuDTO = MenuDTO(
             menuId = it.menuId,
             menuType = it.menuType,
             menu = it.menu,
             price = it.price,
             howToRequest = it.howToRequest,
             isCheck = it.isCheck
        )
        menuDTOList.add(menuDTO)
    }
    val request = RestaurantRequest(
        placeId = this.placeId,
        userId = this.userId,
        title = this.title,
        category = this.category,
        address = this.address,
        phone = this.phone,
        x = this.x,
        y = this.y,
        allVegan = this.allVegan,
        someMenuVegan = this.someMenuVegan,
        ifRequestVegan = this.ifRequestVegan,
        menuArray = menuDTOList
    )
    return request
}


fun List<MarkerDAO>.toMarker() : List<MarkerOfMap> {
    val marker_list = mutableListOf<MarkerOfMap>()
    this.map {
        val marker = MarkerOfMap(
            placeId = it.placeId,
            title = it.title,
            category = it.category,
            veganTypeColor = it.veganTypeColor,
            allVegan = it.allVegan,
            someMenuVegan = it.someMenuVegan,
            ifRequestVegan = it.ifRequestVegan,
            x = it.x,
            y = it.y,
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
        commentCount = this.commentCount,
        bookmark = this.bookmark
    )
}

fun RestaurantInfoResponse.toRestaurantInfo() : RestaurantInfo {
    return RestaurantInfo(
        placeId = this.placeId,
        address =  this.address,
        address2 = this.address2,
        phone = this.phone,
        url = this.url,
        haveTime = this.haveTime,
        shopStatus = this.shopStatus,
        shopHours = this.shopHours,
        updatedTime = this.updatedTime,

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

fun RestaurantTimetableResponse.toRestaurantTimetable() : RestaurantTimetable {
    return RestaurantTimetable(
        mon = OperatingTime(this.mon.today,
            this.mon.open,
        this.mon.`break`),
        tue = OperatingTime(this.tue.today,
            this.tue.open,
            this.tue.`break`),
        wed = OperatingTime(this.wed.today,
            this.wed.open,
            this.wed.`break`),
        thu = OperatingTime(this.thu.today,
            this.thu.open,
            this.thu.`break`),
        fri = OperatingTime(this.fri.today,
            this.fri.open,
            this.fri.`break`),
        sat = OperatingTime(this.sat.today,
            this.sat.open,
            this.sat.`break`),
        sun = OperatingTime(this.sun.today,
            this.sun.open,
            this.sun.`break`)
    )

}

fun ReviewAdding.toRestaurantReviewAddRequest() : RestaurantReviewAddRequest {
    return RestaurantReviewAddRequest(
        commentId = this.commentId,
        placeId = this.placeId,
        userId = this.userId,
        content = this.content
    )
}

fun ReportRestaurant.toRestaurantReportRequest() : RestaurantReportRequest {
    return RestaurantReportRequest(
        placeId = this.placeId,
        userId = this.userId,
        nickname = this.nickname,
        code = this.code
    )
}

fun ReviewReporting.toReportReviewRequest() : ReportReviewRequest {
    return ReportReviewRequest(
        commentId = this.commentId,
        title = this.title,
        createdTime = this.createdTime,
        commentContent = this.commentContent,
        commentNickname = this.commentNickname,
        userId = this.userId,
        nickname = this.nickname,
        code = this.code,
        content = this.content
    )
}

fun PhoneUpdating.toPhoneUpdateRequest() : PhoneUpdateRequest {
    return PhoneUpdateRequest(
        placeId = this.placeId,
        title = this.title,
        userId = this.userId,
        nickname = this.nickname,
        phone = this.phone.toChangedString()
    )
}

fun UrlUpdating.toHomepageUpdateRequest() : HomepageUpdateRequest {
    return HomepageUpdateRequest(
        placeId = this.placeId,
        title = this.title,
        userId = this.userId,
        nickname = this.nickname,
        url = this.url.toChangedString()
    )
}

fun RestaurantInfoUpdating.toRestaurantInfoUpdateRequest(userId : String, nickname : String) : RestaurantInfoUpdateRequest {
    return RestaurantInfoUpdateRequest(
        placeId = this.placeId,
        title = this.title,
        userId = userId,
        nickname = nickname,
        changedTitle = if(this.changedTitle == null) null else this.changedTitle.toChangedString(), //toChangedString()
        category = if(this.category == null) null else this.category.toChangedString(), //this.category.toChangedString(),
        address = if(this.address == null) null else this.address.toChangedString(), // this.address.toChangedString(),
        address2 = if(this.address2 == null) null else this.address2.toChangedString(), //this.address2.toChangedString(),
        x= if(this.x == null) null else this.x.toChangedDouble(), //this.x.toChangedDouble(),
        y = if(this.y == null) null else this.y.toChangedDouble() //this.y.toChangedDouble()
    )
}

fun MenuUpdating.toMenuUpdateRequest(userId : String) : MenuUpdateRequest {
    val updatedMenuDTOList = mutableListOf<MenuDTO>()
    this.updateArray.map {
        val menuDTO = MenuDTO(
            menuId = it.menuId,
            menuType = it.menuType,
            menu = it.menu,
            price = it.price,
            howToRequest = it.howToRequest,
            isCheck = it.isCheck
        )
        updatedMenuDTOList.add(menuDTO)
    }

    val deletedMenuDTOList = mutableListOf<MenuDTO>()
    this.insertArray.map {
        val menuDTO = MenuDTO(
            menuId = it.menuId,
            menuType = it.menuType,
            menu = it.menu,
            price = it.price,
            howToRequest = it.howToRequest,
            isCheck = it.isCheck
        )
        deletedMenuDTOList.add(menuDTO)
    }

    return MenuUpdateRequest(
        placeId = this.placeId,
        userId = userId,
        allVegan = this.allVegan,
        someMenuVegan = this.someMenuVegan,
        ifRequestVegan = this.ifRequestVegan,
        deleteArray = this.deleteArray,
        updateArray = updatedMenuDTOList,
        insertArray = deletedMenuDTOList
    )
}


fun TimetableUpdating.toTimeUpdateRequest(placeId : String, userId : String) : TimeUpdateRequest {
    return TimeUpdateRequest(
        placeId = placeId,
        userId = userId,
        mon = this.mon,
        monBreak = this.monBreak,
        tue = this.tue,
        tueBreak = this.tueBreak,
        wed = this.wed,
        wedBreak = this.wedBreak,
        thu = this.thu,
        thuBreak = this.thuBreak,
        fri = this.fri,
        friBreak = this.friBreak,
        sat = this.sat,
        satBreak = this.satBreak,
        sun = this.sun,
        sunBreak = this.sunBreak
    )
}

fun BeforeAfterString.toChangedString() : ChangedString {

    return ChangedString(
        before = this.before,
        after = this.after
    )
}

fun BeforeAfterDouble.toChangedDouble() : ChangedDouble {
    return ChangedDouble(
        before = this.before,
        after = this.after
    )
}


fun PublicAddressListResponse.toPublicAddressList() : PublicAddressList {

    val publicAddressList = mutableListOf<PublicAddressItem>()
    this.results.juso.map {
        val item = PublicAddressItem(
            roadAddr = it.roadAddr,
            jibunAddr = it.jibunAddr,
            zipNo = it.zipNo,
            )
        publicAddressList.add(item)
    }

       return PublicAddressList(
            totalCount = this.results.common.totalCount,
            currentPage = this.results.common.currentPage,
            countPerPage = this.results.common.countPerPage,
           publicAddressList = publicAddressList
        )
}


fun CoordOfAddressFromKakaoResponse.toCoordiOfPublicAddress() : CoordiOfPublicAddress {
    return CoordiOfPublicAddress(
        x = this.documents[0].x.toDouble(),
        y = this.documents[0].y.toDouble()
    )
}

fun AddressOfCoordFromKakaoResponse.toRoadAddressOfCoordi() : RoadAddressOfCoordi {
    return RoadAddressOfCoordi(
        road_address = this.documents[0].road_address!!.address_name
    )
}