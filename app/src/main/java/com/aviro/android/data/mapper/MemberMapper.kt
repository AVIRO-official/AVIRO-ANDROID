package com.aviro.android.data.mapper


import com.aviro.android.data.model.member.*
import com.aviro.android.domain.entity.member.*


fun toNicknameCheckRequest(nickname : String?) : NicknameCheckRequest {
    return NicknameCheckRequest(
        nickname = nickname
    )
}

fun NicknameCheckResponse.toNicknameCheckResult() : NicknameValidation {
    return NicknameValidation(
        isValid = this.isValid!!,
        message = this.message
    )
}

// 회원가입 요청
fun Member.toCreateMemberRequest() : SignUpRequest {
    return SignUpRequest(
        refreshToken = this.refreshToken,
        accessToken = this.accessToken,
        userId = this.userId,
        userName = this.userName,
        userEmail = this.userEmail,
        nickname = this.nickname,
        birthday = this.birthday,
        gender = this.gender,
        marketingAgree = this.marketingAgree,
        type = this.type
    )
}

fun toNicknameUpdateRequest(user_id : String, new_nickname : String) : NicknameUpdateRequest {
    return NicknameUpdateRequest(
        userId = user_id,
        nickname = new_nickname
    )
}


fun MemberHistoryResponse.toMemberHistory() : MemberHistory {
    return MemberHistory(
        placeCount = this.placeCount,
        commentCount = this.commentCount,
        bookmarkCount = this.bookmarkCount,
    )
}

fun MemberLevelResponse.toMemberLevel() : MemberLevel {
    return MemberLevel(
        level = this.level!!,
        point = this.point!!,
        pointForNext = this.pointForNext!!,
        total = this.total!!,
        userRank = this.userRank!!,
        image = this.image!!

    )
}

fun MemberLevelUpResponse.toMemberLevelUp() : MemberLevelUp {

    return MemberLevelUp(
        levelUp = this.levelUp,
        userLevel = this.userLevel
    )
}

fun MyRestaurantListResponse.toMyRestaurantList() : List<MyRestaurant> {
    //mutableListOf<MyRestaurant>()

    val myRestaurantList = this.placeList.map {
        MyRestaurant(
            placeId = it.placeId,
            title = it.title,
            category = it.category,
            allVegan = it.allVegan,
            someMenuVegan = it.someMenuVegan,
            ifRequestVegan = it.ifRequestVegan,
            shortAddress = it.shortAddress,
            menu = it.menu,
            menuCountExceptOne = it.menuCountExceptOne,
            createdBefore = it.createdBefore
        )
    }.toMutableList()

    return myRestaurantList

}

fun MyCommentListResponse.toMyCommentList() : List<MyComment> {
    val myCommentList = this.commentList.map {
        MyComment(
            commentId = it.commentId,
            placeId = it.placeId,
            content = it.content,
            title = it.title,
            category = it.category,
            allVegan = it.allVegan,
            someMenuVegan = it.someMenuVegan,
            ifRequestVegan = it.ifRequestVegan,
            createdBefore = it.createdBefore
        )
    }.toMutableList()

    return myCommentList
}

fun MyBookmarkListResponse.toMyBookmarkList() : List<MyRestaurant> {
    //mutableListOf<MyRestaurant>()

    val myRestaurantList = this.placeList.map {
        MyRestaurant(
            placeId = it.placeId,
            title = it.title,
            category = it.category,
            allVegan = it.allVegan,
            someMenuVegan = it.someMenuVegan,
            ifRequestVegan = it.ifRequestVegan,
            shortAddress = it.shortAddress,
            menu = it.menu,
            menuCountExceptOne = it.menuCountExceptOne,
            createdBefore = it.createdBefore
        )
    }.toMutableList()

    return myRestaurantList

}

