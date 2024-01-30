package com.android.aviro.presentation.home.ui.mypage

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.data.entity.member.MyInfoLevelResponse
import com.android.aviro.domain.usecase.member.GetMyInfoUseCase
import com.android.aviro.domain.usecase.member.UpdateMyNicnameUseCase
import com.android.aviro.domain.usecase.member.WithdrawUseCas
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import com.android.aviro.presentation.sign.SignNicknameFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    //private val updateMyNicnameUseCase : UpdateMyNicnameUseCase,
    //private val withdrawUseCas : WithdrawUseCas
) : ViewModel() {

    private val _nickname = MutableLiveData<String?>()
    val nickname: LiveData<String?> = _nickname

    private val _registerAmount = MutableLiveData<String>()
    val registerAmount: LiveData<String> = _registerAmount

    private val _reviewAmount = MutableLiveData<String>()
    val reviewAmount: LiveData<String> = _reviewAmount

    private val _bookmarkAmount = MutableLiveData<String>()
    val bookmarkAmount: LiveData<String> = _bookmarkAmount

    private val _challengePeriod = MutableLiveData<String>()
    val challengePeriod: LiveData<String> = _challengePeriod

    private val _challengeTitle = MutableLiveData<String>()
    val challengeTitle: LiveData<String> = _challengeTitle

    // 챌린지 레벨 정보도 하나의 리스트로
    private val _challengeLevelInfo = MutableLiveData<MyInfoLevelResponse>()
    val challengeLevelInfo: LiveData<MyInfoLevelResponse> = _challengeLevelInfo

    // 챌린지 기간

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData


    init {
        _registerAmount.value = ""
        _reviewAmount.value = ""
        _bookmarkAmount.value = ""

        // 챌린지 정보 호출
        getChallengeInfo()
        getMyInfo()
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
            _nickname.value = getMyInfoUseCase.getNickname() + "님의 나무"

            getMyInfoUseCase.getCount().onSuccess {
                if(it.statusCode == 200 && it.data != null) {
                    _registerAmount.value = it.data.placeCount.toString()
                    _reviewAmount.value = it.data.commentCount.toString()
                    _bookmarkAmount.value = it.data.bookmarkCount.toString()
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
               }
           }.onFailure {

           }
        }


    }


    fun onLinkClick(view: View) {
        val id = view.id
        var websiteUrl: String? = null

        when (id) {
            R.id.manubar_nickname -> {
                /*
                val fragmentManager = childFragmentManager.beginTransaction()
                fragmentManager.setCustomAnimations(
                    R.anim.slide_right_enter, // 오른쪽에서 들어올 때의 애니메이션
                    R.anim.slide_left_exit,      // 왼쪽으로 나갈 때의 애니메이션
                    R.anim.slide_left_enter,   // 왼쪽에서 들어올 때의 애니메이션
                    R.anim.slide_right_exit      // 오른쪽으로 나갈 때의 애니메이션
                )
                fragmentManager.replace(R.id., SignNicknameFragment())
                    .addToBackStack("mypage_fragment")
                    .commit()

                 */
            }
            R.id.manubar_question -> {
                websiteUrl = ""
            }
            R.id.manubar_insta -> {
                // 인스타 연결

            }
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
}