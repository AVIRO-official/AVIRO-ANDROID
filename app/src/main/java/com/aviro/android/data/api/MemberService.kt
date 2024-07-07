package com.aviro.android.data.api



import com.aviro.android.data.model.auth.SignInResponse
import com.aviro.android.data.model.base.BaseResponse
import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.member.AddBookmarkRequest
import com.aviro.android.data.model.member.MemberHistoryResponse
import com.aviro.android.data.model.member.MemberLevelResponse
import com.aviro.android.data.model.member.MemberWithdrawRequest
import com.aviro.android.data.model.member.MyBookmarkListResponse
import com.aviro.android.data.model.member.MyCommentListResponse
import com.aviro.android.data.model.member.MyRestaurantListResponse
import com.aviro.android.data.model.member.NicknameCheckRequest
import com.aviro.android.data.model.member.NicknameCheckResponse
import com.aviro.android.data.model.member.NicknameUpdateRequest
import com.aviro.android.data.model.member.SignUpRequest
import com.aviro.android.data.model.member.UpdateReviewRequest
import retrofit2.http.*

interface MemberService {

    @POST("member/check")
    suspend fun checkNickname(
        @Body nickname: NicknameCheckRequest
    ): Result<DataResponse<NicknameCheckResponse>>

    @POST("member/sign-up")
    suspend fun createMember(
        @Body new_member: SignUpRequest
    ): Result<DataResponse<SignInResponse>>

    @GET("mypage/count")
    suspend fun getCount(
        @Query("userId") user_id: String
    ): Result<DataResponse<MemberHistoryResponse>>

    @GET("mypage/challenge/level")
    suspend fun getChallengeLevel(
        @Query("userId") user_id : String
    ): Result<DataResponse<MemberLevelResponse>>

    @POST("member/revoke")
    suspend fun deleteUser(
        @Body request: MemberWithdrawRequest
    ): Result<BaseResponse>



    @POST("member/update/nickname")
    suspend fun updateNickname(
        @Body request: NicknameUpdateRequest
    ): Result<BaseResponse>

    @POST("map/update/bookmark")
    suspend fun addBookmark(
        @Body request: AddBookmarkRequest
    ): Result<BaseResponse>

    @DELETE("map/update/bookmark")
    suspend fun deleteBookmark(
        @Query("placeId") place_id : String,
        @Query("userId") user_id : String
    ): Result<BaseResponse>

    @GET("mypage/place")
    suspend fun getMyRestaurantList(
        @Query("userId") user_id: String
    ): Result<DataResponse<MyRestaurantListResponse>>

    @GET("mypage/comment")
    suspend fun getMyCommentList(
        @Query("userId") user_id: String
    ): Result<DataResponse<MyCommentListResponse>>

    @GET("mypage/bookmark")
    suspend fun getMyBookmarkList(
        @Query("userId") user_id: String
    ): Result<DataResponse<MyBookmarkListResponse>>

    @POST("map/update/comment")
    suspend fun updateReview(
        @Body request: UpdateReviewRequest
    ): Result<BaseResponse>


    @DELETE("map/update/comment")
    suspend fun deleteReview(
        @Query("commentId") commentId: String,
        @Query("userId") user_id: String
    ): Result<BaseResponse>


}