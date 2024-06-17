package com.aviro.android.presentation.sign

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.*
import com.aviro.android.R
import com.aviro.android.domain.entity.auth.Tokens
import com.aviro.android.domain.entity.auth.User
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.domain.entity.key.GOOGLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.domain.entity.key.USER_EMAIL_KEY
import com.aviro.android.domain.entity.key.USER_ID_KEY
import com.aviro.android.domain.entity.key.USER_NAME_KEY
import com.aviro.android.domain.entity.key.USER_NICKNAME_KEY
import com.aviro.android.domain.entity.member.NicknameValidation
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.domain.usecase.auth.AutoSignInUseCase
import com.aviro.android.domain.usecase.auth.CreateTokensUseCase
import com.aviro.android.domain.usecase.auth.GetTokenUseCase
import com.aviro.android.domain.usecase.auth.LogoutUseCase
import com.aviro.android.domain.usecase.auth.ManualSignInUseCase
import com.aviro.android.domain.usecase.member.CreateMemberUseCase
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val createTokensUseCase: CreateTokensUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val autoSignInUseCase: AutoSignInUseCase,
    private val createMemberUseCase: CreateMemberUseCase,
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository,
    private val manualSignInUseCase: ManualSignInUseCase,
    private val logoutUseCase: LogoutUseCase,
)  : ViewModel() {

    var _isSignIn = MutableLiveData<Boolean>()
    val isSignIn : LiveData<Boolean>
        get() = _isSignIn

    val _isSignUp = MutableLiveData<Boolean>()
    val isSignUp : LiveData<Boolean>
        get() = _isSignUp


    val _signType = MutableLiveData<String>()
    val signType : LiveData<String>
        get() = _signType

    val _signUserId = MutableLiveData<String>()
    val signUserId : LiveData<String>
        get() = _signUserId


    val _signEmail = MutableLiveData<String>()
    val signEmail : LiveData<String>
        get() = _signEmail

    val _signName = MutableLiveData<String?>()
    val signName : LiveData<String?>
        get() = _signName



    val _isLoding = MutableLiveData<Boolean>()
    val isLoding : LiveData<Boolean>
        get() = _isLoding

    // 다음으로 버튼
    val _nextBtn = MutableLiveData<Int>()
    val nextBtn : LiveData<Int>
        get() = _nextBtn

    // 다음으로 넘어가도 되는지 확인
    val _isComplete = MutableLiveData<Boolean>()
    val isComplete : LiveData<Boolean>
        get() = _isComplete

    // 회원가입이 완료되었는지 확인 (회원가입 중 앱을 끄면 로그인 정보 삭제)
    val _isCompleteSignUp = MutableLiveData<Boolean>()
    val isCompleteSignUp : LiveData<Boolean>
        get() = _isCompleteSignUp


    val _nicknameText = MutableLiveData<String>()
    val nicknameText : LiveData<String> = _nicknameText

    val _nicknameCountText = MutableLiveData<String>()
    val nicknameCountText : LiveData<String> = _nicknameCountText

    val _nicknameNoticeText = MutableLiveData<String>()
    val nicknameNoticeText : LiveData<String> = _nicknameNoticeText

    val _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid : LiveData<Boolean?> = _isNicknameValid

    val _birthdayText = MutableLiveData<String?>()
    val birthdayText : LiveData<String?> = _birthdayText

    val _birthdayNoticeText = MutableLiveData<String>()
    val birthdayNoticeText : LiveData<String>
        get() = _birthdayNoticeText

    private val _isVaildBirth = MutableLiveData<String>()
    val isVaildBirth: LiveData<String>
        get() = _isVaildBirth

    // 성별
    private val _isGenderList =  MutableLiveData<List<Boolean>>()
    val isGenderList : LiveData<List<Boolean>>
        get() = _isGenderList

    var gender = ""

    // 사용자 동의 약관
    private val _isApproveList =  MutableLiveData<List<Boolean>>() // 모두 true일때만 버튼 활성화
    val isApproveList : LiveData<List<Boolean>>
        get() = _isApproveList

    private val _isAllTrue = MutableLiveData<Boolean>()
    val isAllTrue: LiveData<Boolean>
        get() = _isAllTrue

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?> get() = _errorLiveData


    init {
            _isLoding.value = false
            _isSignIn.value = true
            _nicknameCountText.postValue("(0/8)")
            _nicknameNoticeText.postValue("이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다.")
            _isNicknameValid.postValue(null)

            _birthdayNoticeText.postValue("태어난 연도를 입력해주세요 (선택)")
            _birthdayText.value = null

            _isGenderList.value =  listOf(false, false, false)
            _isApproveList.value = listOf(false, false, false)

            _isAllTrue.value = false
            _isVaildBirth.value = "default"
            _isComplete.value = false

            _isCompleteSignUp.value = false

    }



    // 애플에서만 사용
    fun createTokens(type : String, id_token : String, auth_code : String, email : String, name : String?) {
        viewModelScope.launch {
            _isLoding.value = true

           createTokensUseCase(id_token, auth_code).let {
               when(it){
                   is MappingResult.Success<*> -> {
                       val data = it.data as Tokens
                        if(data.isMember) { // 회원임
                            when(type) {
                                APPLE -> {
                                    // 갑자기 통신 장애 등 발생할 수는
                                    memberRepository.saveMemberInfoToLocal(USER_ID_KEY, it.data.userId)
                                    memberRepository.saveMemberInfoToLocal(USER_EMAIL_KEY, email)
                                    name?.let { memberRepository.saveMemberInfoToLocal(USER_NAME_KEY,  it) }
                                    authRepository.saveSignTypeToLocal(type)

                                    autoSignInUseCase().let {//token
                                        when(it){
                                            is MappingResult.Success<*> -> {
                                                _isSignUp.value = false
                                            }
                                            is MappingResult.Error -> {
                                                // 토큰 발급 받아서 자동로그인 하려고 하는데 에러나는 경우
                                                _errorLiveData.value = it.message // 애플로그인 이후 자동로그인 해야 하는데 안 되는 경우
                                            }
                                            else -> {}
                                        }

                                    }
                                }

                            }
                       } else {
                           _signUserId.value = it.data.userId
                            _signType.value = type
                            _signName.value = name
                            _signEmail.value = email
                            _isSignUp.value = true
                        }
                   }
                   is MappingResult.Error -> _errorLiveData.value = it.message
                   else -> {}
                   }
               }
            _isLoding.value = false
           }
    }



    fun isSignUp()  {
        viewModelScope.launch {
            // 서버로부터 멤버 여부, 닉네임 정보 반환
            manualSignInUseCase(_signUserId.value!!).let {
                when(it) {
                    is MappingResult.Success<*> -> {
                        val data = it.data as User
                        if(data.isMember) {
                            // 회원이므로 저장
                            memberRepository.saveMemberInfoToLocal(USER_ID_KEY, _signUserId.value!!)
                            memberRepository.saveMemberInfoToLocal(USER_EMAIL_KEY, _signEmail.value!!)
                            memberRepository.saveMemberInfoToLocal(USER_NAME_KEY, _signName.value ?: "")
                            authRepository.saveSignTypeToLocal(_signType.value!!)
                            memberRepository.saveMemberInfoToLocal(USER_NICKNAME_KEY, data.nickname!!)
                            _isSignUp.value = false
                        } else {
                            // 회원가입 필요
                            _isCompleteSignUp.value = false
                            _isSignUp.value = true
                        }
                    }

                    is MappingResult.Error -> {
                        _errorLiveData.value = it.message
                    }
                }
            }

        }
    }

    // 회원가입 중 취소
    fun cancelSignUp() {
        viewModelScope.launch {
            when(_signType.value) {
                KAKAO -> {
                    UserApiClient.instance.logout {}
                    UserApiClient.instance.unlink {}

                }
                NAVER -> {
                    NaverIdLoginSDK.logout()
                    NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
                        override fun onSuccess() {}
                        override fun onFailure(httpStatus: Int, message: String) {}
                        override fun onError(errorCode: Int, message: String) {
                            onFailure(errorCode, message)
                        }
                    })
                }
                GOOGLE -> {
                    GoogleSignInManager.getClient().signOut()
                    GoogleSignInManager.getClient().revokeAccess()
                }
            }
        }

    }


    // 닉네임 조건 확인
    fun checkNickname(editable : EditText) {
        val text = editable.text.toString()
        val id = editable.id
        when(id) {
            R.id.editTextNickName -> {
                _nicknameCountText.value = "(${text.length}/8)"
                _nicknameText.value = text

                viewModelScope.launch {
                createMemberUseCase.checkNickname(text).let {
                    when(it) {
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

    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int) {
        val birth = s.toString()
        if(birth.length == 5) {
            val year = birth.substring(0, 4)
            if(birth[4] != '.') {
                _birthdayText.value = "$year.${birth[4]}" //${birth[4]}
            }

        } else if(birth.length == 8){
            val year_month = birth.substring(0, 7)
            if(birth[7] != '.') {
                _birthdayText.value = "$year_month.${birth[7]}" //${birth[7]}
            }
        } else if(birth.length == 10) {
            _birthdayText.value = birth.substring(0, 10)
        }


        if (birth.length == 0) {
            _isVaildBirth.value = "default"
            _birthdayNoticeText.value = "태어난 연도를 입력해주세요 (선택)"
        } else {

            if(checkValidBirth(birth)) {
                _isVaildBirth.value = "true"
                _birthdayNoticeText.value = "태어난 연도를 입력해주세요 (선택)"
            } else {
                _isVaildBirth.value = "false"
                _birthdayNoticeText.value = "올바른 형식으로 입력해주세요"
            }

        }

    }

    fun afterTextChanged(edittext: EditText) {
        val text = edittext.text.toString()
        edittext.setSelection(text.length)
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

    }


    fun checkValidBirth(birthday : String) : Boolean {
        // 날짜 형식 지정
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        // 날짜 파싱
        dateFormat.setLenient(false) // 엄격한 모드로 설정하여 잘못된 날짜를 거부
        return try {
            val parsedDate: Date = dateFormat.parse(birthday)
            // 현재 날짜와 비교하여 미래의 날짜는 유효하지 않도록 검사
            val currentDate = Date()
            parsedDate.before(currentDate)
        } catch (e: ParseException) {
            // 날짜 파싱 중 오류 발생 시 유효하지 않은 날짜로 처리
            false
        }
        true
    }

    fun onClickGender(view : View) {
        val id = view.id
        when(id) {
             R.id.male -> {
                _isGenderList.value =  listOf(true, false, false)
                 gender = "male"
            }
            R.id.female -> {
                _isGenderList.value =  listOf(false, true, false)
                gender = "female"
            }
            R.id.others -> {
                _isGenderList.value =  listOf(false, false, true)
                gender = "others"
            }
        }
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

    fun onClickSignUp() {
        // 회원가입
        // 애플, 구글, 카카오, 네이버 별로 다르게

        val birth =  if(birthdayText.value != null) birthdayText.value.toString().replace(".", "").toInt() else null
        val nickname = nicknameText.value.toString()

        viewModelScope.launch {
            createMemberUseCase(_signUserId.value!! , _signName.value ?: "" , _signEmail.value!! , _signType.value!! , nickname, birth, gender, marketingAgree = true).let { it ->
                when (it) {
                    is MappingResult.Success<*> -> {
                        _isComplete.value = true
                        _isCompleteSignUp.value = true
                    }
                    is MappingResult.Error -> {
                        Log.e("Sign","회원가입 실패")
                        _isComplete.value = false
                        _errorLiveData.value = it.message

                    }
                    else -> {}
                }
            }


        }
    }

}