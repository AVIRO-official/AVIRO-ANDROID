package com.android.aviro.presentation.home.ui.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.data.entity.member.MyInfoLevelResponse
import com.android.aviro.domain.usecase.member.CreateMemberUseCase
import com.android.aviro.domain.usecase.member.GetMyInfoUseCase
import com.android.aviro.domain.usecase.member.UpdateMyNicnameUseCase
import com.android.aviro.domain.usecase.member.WithdrawUseCas
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import com.android.aviro.presentation.sign.SignNicknameFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    private val createMemberUseCase : CreateMemberUseCase,
    private val updateMyNicnameUseCase : UpdateMyNicnameUseCase,
    @ApplicationContext private val context: Context

) : ViewModel() {

    val _nicknameText = MutableLiveData<String>()
    val nicknameText : LiveData<String> = _nicknameText

    val _nicknameCountText = MutableLiveData<String>()
    val nicknameCountText : LiveData<String> = _nicknameCountText

    val _nicknameNoticeText = MutableLiveData<String>()
    val nicknameNoticeText : LiveData<String> = _nicknameNoticeText

    val _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid : LiveData<Boolean?> = _isNicknameValid

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
    private val _challengeLevelInfo = MutableLiveData<MyInfoLevelResponse>()
    val challengeLevelInfo: LiveData<MyInfoLevelResponse> = _challengeLevelInfo


    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    val _toastLiveData = MutableLiveData<String?>()
    val toastLiveData: LiveData<String?> get() = _toastLiveData


    init {
        // 챌린지 정보 호출
        //getChallengeInfo()
        //getMyInfo()
    }

    fun getChallengeInfo() {
        viewModelScope.launch {
            getMyInfoUseCase.getChallengeInfo().onSuccess {
                if(it.statusCode == 200) {
                    _challengePeriod.value = it.period
                    _challengeTitle.value = it.title
                }
            }.onFailure {
                _errorLiveData.value = it.message
            }
        }

    }

    fun getMyInfo() {
        viewModelScope.launch {
            _nickname.value = getMyInfoUseCase.getNickname() //+ "님의 나무"

            getMyInfoUseCase.getCount().onSuccess {
                if(it.statusCode == 200 && it.data != null) {
                    _registerAmount.value = it.data.placeCount.toString() + "개"
                    _reviewAmount.value = it.data.commentCount.toString() + "개"
                    _bookmarkAmount.value = it.data.bookmarkCount.toString() + "개"
                } else {
                    _errorLiveData.value = it.message
                }
            }.onFailure {
                _errorLiveData.value = it.message
            }


           getMyInfoUseCase.getChallengeLevel().onSuccess {
               if(it.statusCode == 200) {
                   // 챌린지 레벨 정보
                   _challengeLevelInfo.value = it
                   _challengeLevel.value = "레벨 ${it.level} 달성했어요!"
                   _challengeMyPoint.value = it.point.toString()
                   _challengeNextPoint.value = "/" + it.pointForNext.toString() + "P"

                   // 챌린지 참여 전인지
                   if(it.userRank == 0) {
                       _challengeRank.value = "포인트를 모아 나무를 성장시켜보세요!"
                   } else {
                       _challengeRank.value = "참여중인 ${it.total}명 중 ${it.userRank}등에요!"
                   }

               }
           }.onFailure {

           }
        }


    }

    fun checkNickname(editable : EditText) {
        val text = editable.text.toString()
        val id = editable.id
        when(id) {
            R.id.editTextNickName -> {
                _nicknameCountText.value = "(${text.length}/8)"

                viewModelScope.launch {
                    val response = createMemberUseCase.checkNickname(text)
                    response.onSuccess {
                        if(it.statusCode == 200) {
                            _nicknameNoticeText.value = it.message

                            if(it.isValid!!) {
                                //_nicknameNoticeTextColor.value = R.color.Gray2
                                _isNicknameValid.value = true
                                _nicknameText.value = text
                            } else {
                                //_nicknameNoticeTextColor.value = R.color.Warn_Red
                                _isNicknameValid.value = false
                            }

                        } else {
                            // 400 : 요청값 오류 500 : 서버 오류
                            _errorLiveData.value = it.message

                            _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
                            //_nicknameNoticeTextColor.value = R.color.Gray2
                            _isNicknameValid.value = false
                        }

                    }.onFailure {
                        _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
                        //_nicknameNoticeTextColor.value = R.color.Gray2
                        _isNicknameValid.value = false
                    }
                }

            }

        }

    }

    fun onLinkClick(view: View) {
        val id = view.id
        var websiteUrl: String? = null

        when (id) {
            R.id.menubar_terms1 -> {
                websiteUrl = "https://sponge-nose-882.notion.site/259b51ac0b4a41d7aaf5ea2b89a768f8?pvs=4"
            }
            R.id.menubar_terms2 -> {
                websiteUrl = "https://sponge-nose-882.notion.site/c98c9103ebdb44cfadd8cd1d11600f99?pvs=4"
            }
            R.id.menubar_terms3 -> {
                websiteUrl = "https://sponge-nose-882.notion.site/50102bd385664c89ab39f1b290fb033e?pvs=4"
            }
            R.id.menubar_opensource -> {
                websiteUrl = ""
            }
            R.id.menubar_thanks -> {
                websiteUrl = "https://bronzed-fowl-e81.notion.site/8b6eb5da64054f7db1c307dd5d057317"
            }
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
        view.context.startActivity(intent)
    }

    fun onClickChangeNickname() {
        viewModelScope.launch {
            updateMyNicnameUseCase(_nicknameText.value.toString()).onSuccess {
                val code = it.statusCode
                if(code == 200) {
                    _nickname.value = getMyInfoUseCase.getNickname() //+ "님의 나무"
                    _toastLiveData.value = code.toString()

                } else {
                    _errorLiveData.value = it.message
                }

            }.onFailure {
                _errorLiveData.value = it.message
            }
        }

    }


}