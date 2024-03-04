package com.android.aviro.data.repository

import com.android.aviro.data.datasource.challenge.ChallengeDataSource
import com.android.aviro.data.mapper.toChallengeInfo
import com.android.aviro.data.model.challenge.ChallengeCommentResponse
import com.android.aviro.data.model.challenge.ChallengeInfoResponse
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.ChallengeRepository
import javax.inject.Inject

class ChallengeRepositoryImp @Inject constructor(
    private val challengeDataSource : ChallengeDataSource
) : ChallengeRepository {

    override suspend fun getChallengeInfo() : MappingResult {
        val response = challengeDataSource.getChallengeInfo()
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null){
                result = MappingResult.Success(null, data.toChallengeInfo())
            }else{
                result = MappingResult.Error("알 수 없는 오류가 발생해 챌린지 정보를 불러올 수 없습니다.")
            }

        }.onFailure {
            result = MappingResult.Error(it.message)

        }
        return result

    }

    override fun getChallengeComment() : MappingResult {
        TODO()
    }

}