package com.aviro.android.data.mapper


import com.aviro.android.data.model.challenge.*
import com.aviro.android.domain.entity.challenge.*


fun ChallengeInfoResponse.toChallengeInfo() : ChallengeInfo {
    return ChallengeInfo(
        period = this.period,
        title = this.title
    )
}

fun ChallengeCommentResponse.toChallengeComment() : ChallengeComment {
    return ChallengeComment(
        comment = this.comment
    )
}


fun ChallengePopUpResponse.toChallengePopUp() : ChallengePopUp {
    return ChallengePopUp(
        imageUrl = this.imageUrl
    )
}
