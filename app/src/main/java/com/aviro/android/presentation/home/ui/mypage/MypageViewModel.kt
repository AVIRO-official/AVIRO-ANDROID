package com.aviro.android.presentation.home.ui.mypage

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.R
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.challenge.ChallengeInfo
import com.aviro.android.domain.entity.member.*
import com.aviro.android.domain.usecase.auth.GetTokenUseCase
import com.aviro.android.domain.usecase.auth.LogoutUseCase
import com.aviro.android.domain.usecase.challenge.GetChallengeInfo
import com.aviro.android.domain.usecase.member.CreateMemberUseCase
import com.aviro.android.domain.usecase.member.DeleteMyReviewUseCase
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import com.aviro.android.domain.usecase.member.UpdateBookmarkUseCase
import com.aviro.android.domain.usecase.member.UpdateMyNicnameUseCase
import com.aviro.android.domain.usecase.member.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    private val getTokenUseCase : GetTokenUseCase,
    private val createMemberUseCase : CreateMemberUseCase,
    private val logoutUseCase : LogoutUseCase,
    private val withdrawUseCase : WithdrawUseCase,
    private val updateMyNicnameUseCase : UpdateMyNicnameUseCase,
    private val updateBookmarkUseCase : UpdateBookmarkUseCase,
    private val getChallengeInfo : GetChallengeInfo,
    private val deleteMyReviewUseCase : DeleteMyReviewUseCase

) : ViewModel() {

    val _nicknameText = MutableLiveData<String>()
    val nicknameText: LiveData<String> = _nicknameText

    val _nicknameCountText = MutableLiveData<String>()
    val nicknameCountText: LiveData<String> = _nicknameCountText

    val _nicknameNoticeText = MutableLiveData<String>()
    val nicknameNoticeText: LiveData<String> = _nicknameNoticeText

    val _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid: LiveData<Boolean?> = _isNicknameValid

    private val _nickname = MutableLiveData<String?>().apply {
        value = ""
    }
    val nickname: LiveData<String?> = _nickname

    private val _registerAmount = MutableLiveData<String>().apply {
        value = "개"
    }
    val registerAmount: LiveData<String> = _registerAmount

    private val _reviewAmount = MutableLiveData<String>().apply {
        value = "개"
    }
    val reviewAmount: LiveData<String> = _reviewAmount

    private val _bookmarkAmount = MutableLiveData<String>().apply {
        value = "개"
    }
    val bookmarkAmount: LiveData<String> = _bookmarkAmount

    private val _challengePeriod = MutableLiveData<String>().apply {
        value = ""
    }
    val challengePeriod: LiveData<String> = _challengePeriod

    private val _challengeTitle = MutableLiveData<String>().apply {
        value = ""
    }
    val challengeTitle: LiveData<String> = _challengeTitle

    private val _challengeRank = MutableLiveData<String>().apply {
        value = ""
    }
    val challengeRank: LiveData<String> = _challengeRank

    private val _challengeLevel = MutableLiveData<String>().apply {
        value = ""
    }
    val challengeLevel: LiveData<String> = _challengeLevel

    private val _challengeMyPoint = MutableLiveData<String>().apply {
        value = ""
    }
    val challengeMyPoint: LiveData<String> = _challengeMyPoint

    private val _challengeNextPoint = MutableLiveData<String>().apply {
        value = ""
    }
    val challengeNextPoint: LiveData<String> = _challengeNextPoint

    // 챌린지 레벨 정보도 하나의 리스트로
    private val _challengeLevelInfo = MutableLiveData<MemberLevel>()
    val challengeLevelInfo: LiveData<MemberLevel> = _challengeLevelInfo

    private val _myRestaurantList = MutableLiveData<List<MyRestaurant>>()
    val myRestaurantList: LiveData<List<MyRestaurant>> = _myRestaurantList

    private val _myBookmarkList = MutableLiveData<List<MyRestaurant>>()
    val myBookmarkList: LiveData<List<MyRestaurant>> = _myBookmarkList

    private val _myReviewList = MutableLiveData<List<MyComment>>()
    val myReviewList: LiveData<List<MyComment>> = _myReviewList

    val _isMyList = MutableLiveData<Boolean>()
    val isMyList: LiveData<Boolean> = _isMyList

    val _socialType = MutableLiveData<String>()
    val socialType: LiveData<String> = _socialType


    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    val _toastLiveData = MutableLiveData<String?>()
    val toastLiveData: LiveData<String?> get() = _toastLiveData

    val _isLoding = MutableLiveData<Boolean>()
    val isLoding: LiveData<Boolean>
        get() = _isLoding

    val _movingScreen = MutableLiveData<String>()
    val movingScreen: LiveData<String> get() = _movingScreen

    val _isBookMarkScreen = MutableLiveData<Boolean>()
    val isBookMarkScreen: LiveData<Boolean> get() = _isBookMarkScreen


    init {
        _isLoding.value = false

        getSignType() // 소셜로그인 타입

        getMyInfo()  // 챌린지 정보 호출
        getChallengeInfo()

        getMyReviewList()
        getMyRestaurantList()
        getMyBookmarkList()


    }

    fun getSignType() {
        viewModelScope.launch {
            _socialType.value = getTokenUseCase.getTokenType() + " 계정"
        }
    }

    fun getChallengeInfo() {
        viewModelScope.launch {
            getChallengeInfo.getChallengeInfo().let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as ChallengeInfo
                        _challengePeriod.value = data.period
                        _challengeTitle.value = data.title
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }
            }
        }

    }

    fun getMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase.getNickname().let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        _nicknameText.value = it.data.toString()
                        _nickname.value = it.data.toString()
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }

                getMyInfoUseCase.getCount().let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            val data = it.data as MemberHistory
                            _registerAmount.value = data.placeCount.toString() + "개"
                            _reviewAmount.value = data.commentCount.toString() + "개"
                            _bookmarkAmount.value = data.bookmarkCount.toString() + "개"
                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message
                        }
                    }

                    getMyInfoUseCase.getChallengeLevel().let {
                        when (it) {
                            is MappingResult.Success<*> -> {
                                val data = it.data as MemberLevel
                                _challengeLevelInfo.value = data
                                _challengeLevel.value = "레벨 ${data.level} 달성했어요!"
                                _challengeMyPoint.value = data.point.toString()
                                _challengeNextPoint.value = "/" + data.pointForNext.toString() + "P"

                                if (data.userRank == 0) {
                                    _challengeRank.value = "포인트를 모아 나무를 성장시켜보세요!"
                                } else {
                                    _challengeRank.value =
                                        "참여중인 ${data.total}명 중 ${data.userRank}등에요!"
                                }
                            }
                            is MappingResult.Error -> {
                                _errorLiveData.value = it.message
                            }
                        }
                    }

                }
            }
        }
    }


    fun getMyRestaurantList() {
        viewModelScope.launch {
            getMyInfoUseCase.getMyRestaurantList().let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as List<MyRestaurant>
                        _myRestaurantList.value = data
                        //_isMyList.value = (data.size != 0)
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }
            }
        }
    }

    fun getMyReviewList() {
        viewModelScope.launch {
            getMyInfoUseCase.getMyCommentList().let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as List<MyComment>
                        _myReviewList.value = data
                        //_isMyList.value = (data.size != 0)
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }
            }
        }
    }

    fun getMyBookmarkList() {
        viewModelScope.launch {
            getMyInfoUseCase.getMyBookmarkList().let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as List<MyRestaurant>
                        _myBookmarkList.value = data
                        //_isMyList.value = (data.size != 0)
                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }
            }
        }
    }

        fun afterTextChanged(s : Editable) {
            val text = s.toString()
            _nicknameCountText.value = "(${text.length}/8)"
            _nickname.value = text

            viewModelScope.launch {
                createMemberUseCase.checkNickname(text).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            val data = it.data as NicknameValidation
                            _isNicknameValid.value = data.isValid
                            _nicknameNoticeText.value = data.message
                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message
                        }
                    }
                }

            }
        }


        fun onClickChangeNickname() {
            viewModelScope.launch {
                updateMyNicnameUseCase(_nickname.value.toString()).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            Log.d("onClickChangeNickname", "닉네임 업뎃 성공 : ${_nickname.value}")

                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message
                        }
                    }
                }

            }
        }

    fun updateBookmark(placeId : String, isLike : Boolean) {
        viewModelScope.launch {
            if(isLike){
                 // 즐겨찾기 해제
                updateBookmarkUseCase.deleteBookmark(placeId)
            } else {
                updateBookmarkUseCase.addBookmark(placeId)
            }
        }
    }

        fun onLinkClick(view: View) {
            val id = view.id
            var websiteUrl: String? = null

            when (id) {
                R.id.menubar_terms1 -> {
                    websiteUrl =
                        "https://sponge-nose-882.notion.site/259b51ac0b4a41d7aaf5ea2b89a768f8?pvs=4"
                }
                R.id.menubar_terms2 -> {
                    websiteUrl =
                        "https://sponge-nose-882.notion.site/c98c9103ebdb44cfadd8cd1d11600f99?pvs=4"
                }
                R.id.menubar_terms3 -> {
                    websiteUrl =
                        "https://sponge-nose-882.notion.site/50102bd385664c89ab39f1b290fb033e?pvs=4"
                }
                R.id.menubar_thanks -> {
                    websiteUrl =
                        "https://bronzed-fowl-e81.notion.site/8b6eb5da64054f7db1c307dd5d057317"
                }
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            view.context.startActivity(intent)
        }

        fun logout() {
            viewModelScope.launch {
                logoutUseCase().let {
                    // 성공했을 경우 -> 로그인 화면으로, 다이얼로그 창 뜨기
                    _movingScreen.value = R.string.LOGOUT.toString()

                }
            }

        }

        fun withdraw() {
            viewModelScope.launch {
                _isLoding.value = true

                withdrawUseCase().let {
                    Log.d("withdraw","${it}")
                    // 성공했을 경우 -> 다이얼로그 창 뜨기, 로그인 화면으로
                    when (it) {
                        is MappingResult.Success<*> -> {
                            Log.d("WithdrawUseCase","${it.message}")
                            if(it.message != null){
                                _toastLiveData.value = it.message
                            }
                            _movingScreen.value = R.string.WITHDRAW.toString()

                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message

                        }
                    }

                }
                _isLoding.value = false
            }
        }

    fun deleteReview(review : MyComment) {
        viewModelScope.launch {
            deleteMyReviewUseCase(review.commentId).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        // 내 후기 삭제 완료
                        _toastLiveData.value = it.message ?: "후기가 삭제되었습니다."

                    }
                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message ?: "후기 삭제에 실패했습니다.\n다시 시도해주세요."
                    }
                }
            }
        }
    }


    }