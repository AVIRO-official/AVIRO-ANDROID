package com.aviro.android.data.repository

import android.util.Log
import com.aviro.android.data.datasource.datastore.DataStoreDataSource
import com.aviro.android.data.datasource.member.MemberDataSource
import com.aviro.android.data.mapper.*
import com.aviro.android.data.model.member.AddBookmarkRequest
import com.aviro.android.data.model.member.MemberWithdrawRequest
import com.aviro.android.data.model.member.UpdateReviewRequest
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.USER_EMAIL_KEY
import com.aviro.android.domain.entity.key.USER_ID_KEY
import com.aviro.android.domain.entity.key.USER_NAME_KEY
import com.aviro.android.domain.entity.key.USER_NICKNAME_KEY
import com.aviro.android.domain.entity.member.Member
import com.aviro.android.domain.repository.MemberRepository
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
        dataStoreDataSource.removeDataStore(USER_ID_KEY)
        dataStoreDataSource.removeDataStore(USER_NAME_KEY)
        dataStoreDataSource.removeDataStore(USER_EMAIL_KEY)
        dataStoreDataSource.removeDataStore(USER_NICKNAME_KEY)
    }

    suspend override fun checkNickname(nickname : String) : MappingResult {

        val response = memberDataSource.checkNickname(toNicknameCheckRequest(nickname))
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data

            if (code == 200 && data != null) {
                result = MappingResult.Success(it.message, data.toNicknameCheckResult())
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
        val nickname = new_nickname
        val response = memberDataSource.updateNickname(toNicknameUpdateRequest(user_id, nickname))
        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode

            if(code == 200) {
                dataStoreDataSource.writeDataStore(USER_NICKNAME_KEY, nickname) // 로컬 저장 정보도 수정
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

    override suspend fun deleteMember(refresh_token : String, type : String) : MappingResult {
        val response = memberDataSource.deleteMember(MemberWithdrawRequest(refresh_token, type))
        Log.d("deleteMember", "${response}")

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

    override suspend fun addBookmark(place_id : String, user_id : String) : MappingResult {
        val response = memberDataSource.addBookmark(AddBookmarkRequest(place_id, user_id))

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
    override suspend fun deleteBookmark(place_id : String, user_id : String) : MappingResult {
        val response = memberDataSource.deleteBookmark(place_id, user_id)

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



    override suspend fun getMyRestaurantList(userId : String) :  MappingResult {
        val response = memberDataSource.getMyRestaurantList(userId)

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                result = MappingResult.Success(it.message, data.toMyRestaurantList())
            } else {
                result = MappingResult.Error(it.message)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }

    override suspend fun getMyCommentList(userId : String) :  MappingResult {
        val response = memberDataSource.getMyCommentList(userId)

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                result = MappingResult.Success(it.message, data.toMyCommentList())
            } else {
                result = MappingResult.Error(it.message)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }

    override suspend fun getMyBookmarkList(userId : String) :  MappingResult {
        val response = memberDataSource.getMyBookmarkList(userId)

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            if(code == 200 && data != null) {
                result = MappingResult.Success(it.message, data.toMyBookmarkList())
            } else {
                result = MappingResult.Error(it.message)
            }
        }.onFailure {
            result = MappingResult.Error(it.message)
        }
        return result
    }
    override suspend fun deleteReview(commentId : String, userId : String) : MappingResult {
        val response = memberDataSource.deleteReivew(commentId, userId)
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
    override suspend fun updateReview(commentId : String, userId : String ,content : String) : MappingResult {
        val response = memberDataSource.updateReview(UpdateReviewRequest(commentId, userId, content))
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