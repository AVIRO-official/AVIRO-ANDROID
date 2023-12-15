package com.android.aviro.presentation

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.R
import com.android.aviro.presentation.Entity.User
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignViewModel : ViewModel() {

    // 회원가입 정보를 저장할 Entity
    val userDTO = User(-1,"","","",null,null, false)

    // 다음으로 버튼
    val _nextBtn = MutableLiveData<Int>()
    val nextBtn : LiveData<Int>
        get() = _nextBtn

    // 다음으로 넘어가도 되는지 확인
    val _isNext = MutableLiveData<Boolean?>()
    val isNext : LiveData<Boolean?>
        get() = _isNext

    val _nicknameAccountText = MutableLiveData<String>()
    val nicknameAccountText : LiveData<String>
        get() = _nicknameAccountText

    val _nicknameNoticeText = MutableLiveData<String>()
    val nicknameNoticeText : LiveData<String>
        get() = _nicknameNoticeText

    val _nicknameNoticeTextColor= MutableLiveData<Int>()
    val nicknameNoticeTextColor : LiveData<Int>
        get() = _nicknameNoticeTextColor

    val _birthdayText = MutableLiveData<String>()
    val birthdayText : LiveData<String>
        get() = _birthdayText

    private val _isAllTrue = MutableLiveData<Boolean>()
    val isAllTrue: LiveData<Boolean>
        get() = _isAllTrue

    // 사용자 동의 약관
    private val _isApproveList =  MutableLiveData<List<Boolean>>() // 모두 true일때만 버튼 활성화
    val isApproveList : LiveData<List<Boolean>>
        get() = _isApproveList


    init {
        viewModelScope.launch {
            _nicknameAccountText.value = "(0/8)"
            _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
            _nicknameNoticeTextColor.value = R.color.Gray2
            _isApproveList.value = listOf(false, false, false)
            _isAllTrue.value = false
            _isNext.value = null
        }
    }


    // 닉네임 조건 확인
    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int) {
        _nicknameAccountText.value = "(${s.length}/8)"

        if(s.length == 0) {
            _nicknameAccountText.value = "(0/8)"
            _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
            _nicknameNoticeTextColor.value = R.color.Gray2
            _isNext.value = null


        } else {
            // 허용 문자 조건
            if (checkCharacter(s)) {
                // 닉네임 중복 확인
                if (checkDuplicate()) {
                    _nicknameNoticeText.value = "사용할 수 있는 닉네임이에요."
                    _nicknameNoticeTextColor.value = R.color.Gray2
                    _isNext.value = true
                    userDTO.nickname = s.toString()

                } else {
                    _nicknameNoticeText.value = "중복된 닉네임이에요. 다시 시도해주세요."
                    _nicknameNoticeTextColor.value = R.color.Warn_Red
                    _isNext.value = false
                }

            } else {
                _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
                _nicknameNoticeTextColor.value = R.color.Gray2
                _isNext.value = false

            }
        }

    }


    fun afterTextChanged(editable: Editable?) {
        val text = editable.toString()
        if (text.length == 4 || text.length == 7) {
            _birthdayText.value = "${text}" + "."

        } else if (text.length == 10) {
            // 생일 유효성 검증

        }
    }

    fun onClickApprove(view : View) {
        // 클릭한 뷰의 id 추출
        val id = view.id
        when (id) {
            R.id.approveBtn1 -> {
                _isApproveList.value = _isApproveList.value?.mapIndexed { index, value ->
                    if (index == 0) !value else value
                }

            }
            R.id.approveBtn2 -> {
                _isApproveList.value = _isApproveList.value?.mapIndexed { index, value ->
                    if (index == 1) !value else value
                }
            }
            R.id.approveBtn3 -> {
                _isApproveList.value = _isApproveList.value?.mapIndexed { index, value ->
                    if (index == 2) !value else value
                }
            }
            R.id.approveBtnAll -> {
                if(_isApproveList.value!!.all { it == true }) {
                    _isApproveList.value = listOf(false, false, false)
                } else {
                    _isApproveList.value = listOf(true, true, true)
                }

            }

        }

        _isAllTrue.value = _isApproveList.value!!.all {it == true}
        userDTO.marketingAgree = _isApproveList.value!!.all {it == true}

    }

    fun checkDuplicate() : Boolean {

        return true

    }

    fun checkCharacter(nickname : CharSequence) : Boolean{
        val regex = "^[a-zA-Z0-9가-힣-_]+\$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(nickname)

        if(matcher.find()) {
            return true
        }

        return false
    }

    fun onLinkClick(view: View) {
        val id = view.id
        var websiteUrl: String? = null

        when (id) {
            R.id.terms1 -> {
                websiteUrl = "https://sponge-nose-882.notion.site/259b51ac0b4a41d7aaf5ea2b89a768f8?pvs=4"
            }
            R.id.terms2 -> {
                websiteUrl = "https://sponge-nose-882.notion.site/c98c9103ebdb44cfadd8cd1d11600f99?pvs=4"
            }
            R.id.terms3 -> {
                websiteUrl = "https://sponge-nose-882.notion.site/50102bd385664c89ab39f1b290fb033e?pvs=4"
            }
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
        view.context.startActivity(intent)

    }


}