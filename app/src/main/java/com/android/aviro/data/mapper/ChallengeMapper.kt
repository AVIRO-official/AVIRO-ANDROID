package com.android.aviro.data.mapper

import com.android.aviro.data.model.challenge.ChallengeCommentResponse
import com.android.aviro.data.model.challenge.ChallengeInfoResponse
import com.android.aviro.domain.entity.challenge.ChallengeComment
import com.android.aviro.domain.entity.challenge.ChallengeInfo

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