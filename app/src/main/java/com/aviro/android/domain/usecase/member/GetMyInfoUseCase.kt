package com.aviro.android.domain.usecase.member

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.USER_ID_KEY
import com.aviro.android.domain.entity.key.USER_NICKNAME_KEY
import com.aviro.android.domain.repository.MemberRepository
import javax.inject.Inject

class GetMyInfoUseCase  @Inject constructor (
    private val memberRepository : MemberRepository
) {

    // 사용자 닉네임 정보
    suspend fun getNickname() : MappingResult {
       val nickname = memberRepository.getMemberInfoFromLocal(USER_NICKNAME_KEY)
        nickname?.let {
            return MappingResult.Success("", nickname)
        }
        return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n다시 로그인 해주세요.")

    }

    // 사용자 id 정보
    suspend fun getUserId() : MappingResult {
        // user_id 없는 경우 처리 -> 에러 메세지 바로 띄우기
        val user_id = memberRepository.getMemberInfoFromLocal(USER_ID_KEY)
        user_id?.let {
            return MappingResult.Success("", user_id)
        }
        return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n다시 로그인 해주세요.")
    }

    // 사용자 가게 등록 정보 (활동 정보)
    suspend fun getCount() : MappingResult {
        val userId = getUserId()
        when(userId) {
            is MappingResult.Success<*> -> {
                return memberRepository.getCount(userId.data.toString())
            }
            is MappingResult.Error -> {
                return userId
            }
        }

    }

    // 사용자 챌린지 정보 (레벨 정보)
    suspend fun getChallengeLevel() : MappingResult {
        val userId = getUserId()
        when(userId) {
            is MappingResult.Success<*> -> {
                return memberRepository.getChallengeLevel(userId.data.toString())
            }
            is MappingResult.Error -> {
                return userId
            }
        }

    }


    suspend fun getMyRestaurantList() : MappingResult {
        val user_id = memberRepository.getMemberInfoFromLocal(USER_ID_KEY)
        user_id?.let {
            return memberRepository.getMyRestaurantList(user_id)

        }
            return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n" +
                    "다시 로그인 해주세요.")

    }

    suspend fun getMyCommentList() : MappingResult {
        val user_id = memberRepository.getMemberInfoFromLocal(USER_ID_KEY)
        user_id?.let {
            return memberRepository.getMyCommentList(user_id)
        }
        return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n" +
                "다시 로그인 해주세요.")


    }
    suspend fun getMyBookmarkList() : MappingResult {
        val user_id = memberRepository.getMemberInfoFromLocal(USER_ID_KEY)
        user_id?.let {
            return memberRepository.getMyBookmarkList(user_id)

        }
        return MappingResult.Error("회원 정보를 찾을 수 없습니다.\n" +
                "다시 로그인 해주세요.")


    }


}