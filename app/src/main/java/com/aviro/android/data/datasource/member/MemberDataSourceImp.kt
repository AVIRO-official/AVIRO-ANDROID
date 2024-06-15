package com.aviro.android.data.datasource.member

import com.aviro.android.data.api.MemberService
import com.aviro.android.data.model.auth.SignInResponse
import com.aviro.android.data.model.base.BaseResponse
import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.member.*

import javax.inject.Inject

class MemberDataSourceImp @Inject constructor (
    private val memberService : MemberService
): MemberDataSource {


    override suspend fun checkNickname(nickname : NicknameCheckRequest) : Result<DataResponse<NicknameCheckResponse>> {
        return memberService.checkNickname(nickname)
    }

    override suspend fun creatMember(newMember: SignUpRequest): Result<DataResponse<SignInResponse>> {
        return memberService.createMember(newMember)
        }


    override suspend fun getActivityCount(request : String) : Result<DataResponse<MemberHistoryResponse>> {
        return memberService.getCount(request)

    }

    override suspend fun getChallengeLevel(request : String) : Result<DataResponse<MemberLevelResponse>> {
        return memberService.getChallengeLevel(request)

    }

    override suspend fun deleteMember(refresh_token : MemberWithdrawRequest) : Result<BaseResponse> {
        return memberService.deleteUser(refresh_token)

    }

    override suspend fun updateNickname(request : NicknameUpdateRequest) : Result<BaseResponse> {
        return memberService.updateNickname(request)
    }

    override suspend fun addBookmark(request : AddBookmarkRequest) : Result<BaseResponse> {
        return memberService.addBookmark(request)
    }
    override suspend fun deleteBookmark(place_id : String, user_id : String) : Result<BaseResponse> {
        return memberService.deleteBookmark(place_id, user_id)
    }


    override suspend fun getMyRestaurantList(user_id : String) : Result<DataResponse<MyRestaurantListResponse>> {
        return memberService.getMyRestaurantList(user_id)
    }
    override suspend fun getMyCommentList(user_id : String) : Result<DataResponse<MyCommentListResponse>> {
        return memberService.getMyCommentList(user_id)
    }
    override suspend fun getMyBookmarkList(user_id : String) : Result<DataResponse<MyBookmarkListResponse>> {
        return memberService.getMyBookmarkList(user_id)
    }

    override suspend fun deleteReivew(commentId : String, userId: String) : Result<BaseResponse> {
        return memberService.deleteReview(commentId, userId)
    }
    override suspend fun updateReview(request : UpdateReviewRequest) : Result<BaseResponse> {
        return memberService.updateReview(request)
    }



    }
