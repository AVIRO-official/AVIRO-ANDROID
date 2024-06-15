package com.aviro.android.domain.usecase.member

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.MemberRepository
import javax.inject.Inject

class DeleteMyReviewUseCase @Inject constructor(
    private val memberRepository : MemberRepository,
    private val getMyInfoUseCase: GetMyInfoUseCase
) {
    suspend operator fun invoke(commentId: String): MappingResult {

        val userId = getMyInfoUseCase.getUserId()
        userId.let {
            when (it) {
                is MappingResult.Success<*> -> return memberRepository.deleteReview(
                    commentId,
                    it.data as String
                )

                is MappingResult.Error -> return it
            }
        }


        /*   val user_id = memberRepository.getMemberInfoFromLocal("user_id")
        user_id?.let {
         return memberRepository.deleteReview(commentId, user_id)

        }
        return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n다시 로그인 해주세요.")
    }*/
    }
}