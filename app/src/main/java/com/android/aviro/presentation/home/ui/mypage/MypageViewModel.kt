package com.android.aviro.presentation.home.ui.mypage

import android.content.Intent
import android.net.Uri
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

    private val _registerAmount = MutableLiveData<Int>()
    val registerAmount: LiveData<Int> = _registerAmount

    private val _reviewAmount = MutableLiveData<Int>()
    val reviewAmount: LiveData<Int> = _reviewAmount

    private val _bookmarkAmount = MutableLiveData<Int>()
    val bookmarkAmount: LiveData<Int> = _bookmarkAmount

    // 챌린지 레벨 정보도 하나의 리스트로
    private val _challengeLevelInfo = MutableLiveData<MyInfoLevelResponse>()
    val challengeLevelInfo: LiveData<MyInfoLevelResponse> = _challengeLevelInfo

    // 챌린지 기간


    init {
        // 챌린지 정보 호출
        getMyInfo()
    }

    fun getChallengeInfo() {

    }

    fun getMyInfo() {
        viewModelScope.launch {
            _nickname.value = getMyInfoUseCase.getNickname() + "님의 나무"

            getMyInfoUseCase.getCount().onSuccess {
                if(it.statusCode == 200 && it.data != null) {
                    //_registerAmount.value = it.data.placeCount
                    //_reviewAmount.value = it.data.commentCount
                    //_bookmarkAmount.value = it.data.bookmarkCount
                } else {

                    // 에러 메세지?
                }
            }.onFailure {
                // 에러메세지?
            }

           getMyInfoUseCase.getChallengeLevel().onSuccess {
               if(it.statusCode == 200) {
                   // 챌린지 레벨 정보
                   _challengeLevelInfo.value = it
               }
           }.onFailure {  }
        }


    }


    fun onLinkClick(view: View) {
        val id = view.id
        var websiteUrl: String? = null

        when (id) {
            R.id.manubar_nickname -> {
                websiteUrl = ""
            }
            R.id.manubar_question -> {
                websiteUrl = ""
            }
            R.id.manubar_insta -> {
                websiteUrl = ""
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
                websiteUrl = ""
            }

        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
        view.context.startActivity(intent)

    }
}