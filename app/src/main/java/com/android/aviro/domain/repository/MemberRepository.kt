package com.android.aviro.domain.repository

import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.member.*

/** 유저 정보와 관련된 데이터를 이 레포지토리에서 모아 처리합니다.
 * 필요시 응답 결과로 받은 DTO를 유저가 실제로 사용할 DTO로 변화합니다.
 * */
interface MemberRepository {

    suspend fun getMemberInfoFromLocal(key : String) : String?
    suspend fun saveMemberInfoToLocal(key: String, value : String)

    suspend fun creatMember(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : MappingResult //Result<Any>
    suspend fun checkNickname(nickname : NicknameEntity) :  Result<NicknameCheckResponse> // 닉네임 유효성 체크
    suspend fun removeMemberInfoFromLocal() // 유저 정보 삭제 (로그아웃, 탈퇴)
    suspend fun deleteMember(refresh_token : String) : Result<BaseResponse>// 유저 탈퇴 (탈퇴)
    suspend fun updateNickname(request : NicknameUpdateRequest) :  Result<BaseResponse>

    suspend fun getCount(userId : String) :  Result<DataBodyResponse<MyInfoCountResponse>> // 유저의 활동 내역 (후기, 가게, 즐찾)
    suspend fun getChallengeLevel(userId : String) : Result<MyInfoLevelResponse> // 유저 챌린지 레벨 정보

}
