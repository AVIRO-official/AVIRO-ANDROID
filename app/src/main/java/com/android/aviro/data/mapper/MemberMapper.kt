package com.android.aviro.data.mapper

import com.android.aviro.data.model.member.*
import com.android.aviro.domain.entity.member.Member
import com.android.aviro.domain.entity.member.MemberHistory
import com.android.aviro.domain.entity.member.MemberLevel
import com.android.aviro.domain.entity.member.NicknameValidation



fun NicknameCheckResponse.toNicknameCheckResult() : NicknameValidation {
    return NicknameValidation(
        isValid = this.isValid!!,
        message = this.message
    )
}

// 회원가입 요청
fun Member.toCreateMemberRequest() : SignUpRequest {
    return SignUpRequest(
        refreshToken = refreshToken,
        accessToken = accessToken,
        userId = userId,
        userName = userName,
        userEmail = userEmail,
        nickname = nickname,
        birthday = birthday,
        gender = gender,
        marketingAgree = marketingAgree,
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

