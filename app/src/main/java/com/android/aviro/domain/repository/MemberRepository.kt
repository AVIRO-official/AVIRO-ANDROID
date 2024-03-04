package com.android.aviro.domain.repository

import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.member.Member

/** 유저 정보와 관련된 데이터를 이 레포지토리에서 모아 처리합니다.
 * 필요시 응답 결과로 받은 DTO를 유저가 실제로 사용할 DTO로 변화합니다.
 * */
interface MemberRepository {

    suspend fun getMemberInfoFromLocal(key : String) : String?
    suspend fun saveMemberInfoToLocal(key: String, value : String)
    suspend fun removeMemberInfoFromLocal() // 유저 정보 삭제 (로그아웃, 탈퇴)

    suspend fun creatMember(new_member : Member) : MappingResult //Result<Any>
    suspend fun checkNickname(nickname : String) : MappingResult

    suspend fun deleteMember(refresh_token : String) : MappingResult// 유저 탈퇴 (탈퇴)
    suspend fun updateNickname(user_id : String, new_nickname : String) : MappingResult

    suspend fun getCount(userId : String) :  MappingResult // 유저의 활동 내역 (후기, 가게, 즐찾)
    suspend fun getChallengeLevel(userId : String) : MappingResult // 유저 챌린지 레벨 정보

}
