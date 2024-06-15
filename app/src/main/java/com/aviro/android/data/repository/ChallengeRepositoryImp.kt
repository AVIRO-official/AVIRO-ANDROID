package com.aviro.android.data.repository

import android.util.Log
import com.aviro.android.data.mapper.*
import com.aviro.android.data.datasource.challenge.ChallengeDataSource
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.ChallengeRepository
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

    override suspend fun getChallengeComment() : MappingResult {
        val response = challengeDataSource.getChallengeComment()
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null){
                result = MappingResult.Success(null, data.toChallengeComment())
            }else{
                result = MappingResult.Error("알 수 없는 오류가 발생해 챌린지 정보를 불러올 수 없습니다.")
            }

        }.onFailure {
            result = MappingResult.Error(it.message)

        }
        return result
    }

    override suspend fun getChallengePopUp() : MappingResult {
        val response = challengeDataSource.getChallengePopUp()
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200) {
                if(data != null) {
                    Log.d("getChallengePopUp","${data.toChallengePopUp()}")
                    return MappingResult.Success(null, data.toChallengePopUp())
                }
            }

            return MappingResult.Error("${it.message}")

        }.onFailure {
            result = MappingResult.Error(it.message)

        }
        return result
    }

}