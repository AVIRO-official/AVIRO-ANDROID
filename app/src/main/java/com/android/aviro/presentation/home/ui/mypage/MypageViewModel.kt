package com.android.aviro.presentation.home.ui.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.data.model.member.MemberLevelResponse
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.challenge.ChallengeInfo
import com.android.aviro.domain.entity.member.MemberHistory
import com.android.aviro.domain.entity.member.MemberLevel
import com.android.aviro.domain.entity.member.NicknameValidation
import com.android.aviro.domain.usecase.member.CreateMemberUseCase
import com.android.aviro.domain.usecase.member.GetMyInfoUseCase
import com.android.aviro.domain.usecase.member.UpdateMyNicnameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.Challenge
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
    private val _challengeLevelInfo = MutableLiveData<MemberLevel>()
    val challengeLevelInfo: LiveData<MemberLevel> = _challengeLevelInfo


    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData

    val _toastLiveData = MutableLiveData<String?>()
    val toastLiveData: LiveData<String?> get() = _toastLiveData


    init {
        // 챌린지 정보 호출
        getChallengeInfo()
        getMyInfo()
    }

    fun getChallengeInfo() {
        viewModelScope.launch {
            getMyInfoUseCase.getChallengeInfo().let {
                when(it) {
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
            _nickname.value = getMyInfoUseCase.getNickname() //+ "님의 나무"


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
                                _challengeRank.value = "참여중인 ${data.total}명 중 ${data.userRank}등에요!"
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

        fun checkNickname(editable: EditText) {
            val text = editable.text.toString()
            val id = editable.id
            when (id) {
                R.id.editTextNickName -> {
                    _nicknameCountText.value = "(${text.length}/8)"

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

            }

        }

        fun onClickChangeNickname() {
            viewModelScope.launch {
                updateMyNicnameUseCase(_nicknameText.value.toString()).let {
                    when (it) {
                        is MappingResult.Success<*> -> {
                            _nickname.value = getMyInfoUseCase.getNickname()
                            _toastLiveData.value = it.message
                        }
                        is MappingResult.Error -> {
                            _errorLiveData.value = it.message
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
                R.id.menubar_opensource -> {
                    websiteUrl = ""
                }
                R.id.menubar_thanks -> {
                    websiteUrl =
                        "https://bronzed-fowl-e81.notion.site/8b6eb5da64054f7db1c307dd5d057317"
                }
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            view.context.startActivity(intent)
        }



}