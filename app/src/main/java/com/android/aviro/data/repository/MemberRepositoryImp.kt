package com.android.aviro.data.repository

import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.datasource.member.MemberDataSource
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.member.*
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

    override suspend fun creatMember(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : MappingResult { //Result<Any>
        val access_token = dataStoreDataSource.readDataStore("access_token").toString()
        val refresh_token = dataStoreDataSource.readDataStore("refresh_token").toString()
        val user_id = dataStoreDataSource.readDataStore("user_id").toString()
        val user_name = dataStoreDataSource.readDataStore("user_name").toString() // 현재 "" 임
        val user_email = dataStoreDataSource.readDataStore("user_email").toString()

        val new_member = MemberEntity(access_token, refresh_token, user_id, user_name, user_email, nickname, birth, gender, marketingAgree = true)
        val response = memberDataSource.creatMember(new_member)
        lateinit var  result : MappingResult

        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if (code == 200 && data != null) {
                result = MappingResult.Success(it.statusCode, it.message, it.data)
            } else {
                // 서버 내부 에러
                when(code){
                    400 -> result = MappingResult.Error(it.statusCode, it.message ?: "잘못된 요청 입니다.\n처음부터 다시 진행해주세요")
                    500 -> result = MappingResult.Error(it.statusCode, it.message ?: "서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.statusCode, it.message ?: "알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                }
            }
        }.onFailure {
            // 통신 에러
            result =  MappingResult.Error(0, it.message)
        }

        return result

    }

    suspend override fun checkNickname(nickname : NicknameEntity) : Result<NicknameCheckResponse> {
        return memberDataSource.checkNickname(nickname)
    }

    override suspend fun getCount(userId : String) : Result<DataBodyResponse<MyInfoCountResponse>> {
        return memberDataSource.getActivityCount(userId)

    }

    override suspend fun getChallengeLevel(userId : String) : Result<MyInfoLevelResponse> {
       return memberDataSource.getChallengeLevel(userId)

    }


}