package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.review.Review
import com.aviro.android.domain.entity.review.ReviewReporting
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject

class ReportReviewUseCase @Inject constructor (
    private val restaurantRepository : RestaurantRepository,
    private val memberRepository : MemberRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase
) {
    operator suspend fun invoke(
        placeName: String,
        reviewInfo: Review,
        reportNum: Int,
        content: String?
    ): MappingResult {

        val userId = getMyInfoUseCase.getUserId()
        val nickname = getMyInfoUseCase.getNickname()

        userId.let { user_id ->
            when (user_id) {
                is MappingResult.Success<*> -> {
                    nickname.let { nick_name ->
                        when (nick_name) {
                            is MappingResult.Success<*> -> return restaurantRepository.reportReview(
                                ReviewReporting(
                                    reviewInfo.commentId,
                                    placeName,
                                    reviewInfo.updatedTime,
                                    reviewInfo.content,
                                    reviewInfo.nickname,
                                    user_id.data as String,
                                    nick_name.data as String,
                                    reportNum,
                                    content
                                )
                            )
                            is MappingResult.Error -> return nick_name
                        }
                    }
                }

                is MappingResult.Error -> return user_id

            }
        }
    }
}




        //val user_id = memberRepository.getMemberInfoFromLocal("user_id")
        //val nickname = memberRepository.getMemberInfoFromLocal("nickname")

            /*
        user_id?.let {
            nickname?.let {
                return restaurantRepository.reportReview(ReviewReporting(
                    reviewInfo.commentId,
                    placeName,
                    reviewInfo.updatedTime,
                    reviewInfo.content,
                    reviewInfo.nickname,
                    user_id,
                    nickname,
                    reportNum,
                    content
                ))
            }
        }
        return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n다시 로그인 해주세요.")

             */
