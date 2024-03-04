package com.android.aviro.data.repository

import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.datasource.member.MemberDataSource
import com.android.aviro.data.mapper.*
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.member.Member
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject


class MemberRepositoryImp @Inject constructor (
    private val memberDataSource : MemberDataSource,
    private val dataStoreDataSource : DataStoreDataSource
) : MemberRepository {


    override suspend fun getMemberInfoFromLocal(key : String): String? {
        return dataStoreDataSource.readDataStore(key)
    }

    override suspend fun saveMemberInfoToLocal(key: String, value : String) {
        dataStoreDataSource.writeDataStore(key, value)
    }

    // 유저 정보 삭제 (로그아웃, 탈퇴)
    override suspend fun removeMemberInfoFromLocal() {
        // 유저 정보 제거
        dataStoreDataSource.removeDataStore("user_id")
        dataStoreDataSource.removeDataStore("user_name")
        dataStoreDataSource.removeDataStore("user_email")
        dataStoreDataSource.removeDataStore("nickname")
    }

    suspend override fun checkNickname(nickname : String) : MappingResult {
        val response = memberDataSource.checkNickname(nickname)
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            if (code == 200) {
                result = MappingResult.Success(it.message, it.toNicknameCheckResult())
            } else {
                // 서버 내부 에러
                result =  MappingResult.Error(it.message)
            }
        }.onFailure {
            // 통신 에러
            result =  MappingResult.Error(it.message)
        }
        return result
    }

    override suspend fun creatMember(new_member : Member) : MappingResult {
        val response = memberDataSource.creatMember(new_member.toCreateMemberRequest())

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if (code == 200 && data != null) {
                result = MappingResult.Success(it.message, it.data.toSignIn())
            } else {
                // 서버 내부 에러
                when(code){
                    400 -> result = MappingResult.Error(it.message ?: "잘못된 요청으로 회원가입에 실패했습니다.\n처음부터 다시 진행해주세요")
                    500 -> result = MappingResult.Error(it.message ?: "서버 오류로 회원가입에 실패했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.message ?: "알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                }
            }
        }.onFailure {
            // 통신 에러
            result =  MappingResult.Error( it.message)
        }
        return result
    }

    suspend override fun updateNickname(user_id : String, new_nickname : String) : MappingResult {
        val response = memberDataSource.updateNickname(toNicknameUpdateRequest(user_id, new_nickname))
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            if(code == 200) {
                dataStoreDataSource.writeDataStore("nickname", new_nickname) // 로컬 저장 정보도 수정
                result = MappingResult.Success(it.message, null)
            } else {
                result = MappingResult.Error(it.message)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }


    override suspend fun getCount(userId : String) : MappingResult {
        val response = memberDataSource.getActivityCount(userId)
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                result = MappingResult.Success(null, it.data.toMemberHistory())
            } else {
                result = MappingResult.Error("알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }

    override suspend fun getChallengeLevel(userId : String) : MappingResult {
        val response = memberDataSource.getChallengeLevel(userId)
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data

            if(code == 200 && data != null) {
                result= MappingResult.Success(null, data.toMemberLevel())
            } else {
                result = MappingResult.Error(it.message)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }

    override suspend fun deleteMember(refresh_token : String) : MappingResult {
        val response = memberDataSource.deleteMember(refresh_token)
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            if(code == 200) {
                result = MappingResult.Success(it.message, null)
            } else {
                result = MappingResult.Error(it.message)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }

}