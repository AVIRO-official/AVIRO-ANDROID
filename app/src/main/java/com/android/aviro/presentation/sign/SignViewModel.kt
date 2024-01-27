package com.android.aviro.presentation.sign

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.*
import com.android.aviro.R
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.domain.repository.MemberRepository
import com.android.aviro.domain.usecase.auth.AutoSignInUseCase
import com.android.aviro.domain.usecase.auth.CreateTokensUseCase
import com.android.aviro.domain.usecase.auth.GetTokenUseCase
import com.android.aviro.domain.usecase.member.CreateMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context
)  : ViewModel() {


    var _isSignIn = MutableLiveData<Boolean>()
    val isSignIn : LiveData<Boolean>
        get() = _isSignIn

    val _isSignUp = MutableLiveData<Boolean>()
    val isSignUp : LiveData<Boolean>
        get() = _isSignUp

    // 다음으로 버튼
    val _nextBtn = MutableLiveData<Int>()
    val nextBtn : LiveData<Int>
        get() = _nextBtn

    // 다음으로 넘어가도 되는지 확인
    val _isNicknameValid = MutableLiveData<Boolean>()
    val isNicknameValid : LiveData<Boolean>
        get() = _isNicknameValid

    // 다음으로 넘어가도 되는지 확인
    val _isComplete = MutableLiveData<Boolean>()
    val isComplete : LiveData<Boolean>
        get() = _isComplete

    val _nicknameText = MutableLiveData<String>()
    val nicknameText : LiveData<String>
        get() = _nicknameText

    val _nicknameAccountText = MutableLiveData<String>()
    val nicknameAccountText : LiveData<String>
        get() = _nicknameAccountText

    val _nicknameNoticeText = MutableLiveData<String>()
    val nicknameNoticeText : LiveData<String>
        get() = _nicknameNoticeText

    val _nicknameNoticeTextColor= MutableLiveData<Int>()
    val nicknameNoticeTextColor : LiveData<Int>
        get() = _nicknameNoticeTextColor

    val _birthdayText = MutableLiveData<String?>()
    val birthdayText : LiveData<String?>
        get() = _birthdayText

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

        viewModelScope.launch {
            _isSignIn.value = true
            _nicknameAccountText.value = "(0/8)"
            _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
            _nicknameNoticeTextColor.value = R.color.Gray2
            _birthdayNoticeText.value = "태어난 연도를 입력해주세요 (선택)"
            _birthdayText.value = null
            _isGenderList.value =  listOf(false, false, false)
            _isApproveList.value = listOf(false, false, false)
            _isAllTrue.value = false
            //_isNicknameValid.value = false
            _isVaildBirth.value = "default"
            _isComplete.value = false
        }
    }

    // 사용자 로그인
    fun createTokens(id_token : String, auth_code : String, email:String, name : String?) {
        viewModelScope.launch {
            memberRepository.saveMemberInfoToLocal("user_email", email)
            name?.let { memberRepository.saveMemberInfoToLocal("user_name", it) }
           createTokensUseCase(id_token, auth_code).let {
               when(it){
                   is MappingResult.Success<*> -> {
                       val data = it.data as TokensResponseDTO
                       _isSignUp.value = !(data.isMember)
                      /* if(data.isMember) { // 회원임
                           //autoSignIn() // 가져온 토큰으로 자동 로그인 완료 해야 함
                       } else {
                           _isSignUp.value = true
                       }*/
                   }
                   is MappingResult.Error -> _errorLiveData.value = it.message

                   }
               }
           }

    }

    /*
    fun autoSignIn() {
        viewModelScope.launch {
            val token = getTokenUseCase()
            // 갑자기 통신 장애 등 발생할 수는 있음
            autoSignInUseCase(token!!).let {
                when(it){
                    is MappingResult.Success<*> -> _isSignUp.value = false
                    is MappingResult.Error -> {
                        // 토큰 발급 받아서 자동로그인 하려고 하는데 에러나는 경우
                        _errorLiveData.value = it.message // 애플로그인 이후 자동로그인 해야 하는데 안 되는 경우
                        //_isSignIn.value = false
                    }
                }

            }

        }
    }
     */


    // 닉네임 조건 확인
    fun checkNickname(editable : EditText) {
        val text = editable.text.toString()
        val id = editable.id
        when(id) {
            R.id.editTextNickName -> {
                _nicknameAccountText.value = "(${text.length}/8)"

                viewModelScope.launch {
                val response = createMemberUseCase.checkNickname(text)
                response.onSuccess {
                    if(it.statusCode == 200) {
                        _nicknameNoticeText.value = it.message

                        if(it.isValid!!) {
                            _nicknameNoticeTextColor.value = R.color.Gray2
                            _isNicknameValid.value = true
                            _nicknameText.value = text
                        } else {
                            _nicknameNoticeTextColor.value = R.color.Warn_Red
                            _isNicknameValid.value = false
                        }

                    } else {
                        // 400 : 요청값 오류 500 : 서버 오류
                        _errorLiveData.value = it.message

                        _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
                        _nicknameNoticeTextColor.value = R.color.Gray2
                        _isNicknameValid.value = false
                    }

                }.onFailure {
                    _nicknameNoticeText.value = "이모지, 특수문자(-, _ 제외)를 사용할 수 없습니다."
                    _nicknameNoticeTextColor.value = R.color.Gray2
                    _isNicknameValid.value = false
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
                _birthdayText.value = "$year.${birth[4]}"
            }

        } else if(birth.length == 8){
            val year_month = birth.substring(0, 7)
            if(birth[7] != '.') {
                _birthdayText.value = "$year_month.${birth[7]}"
            }

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
        //userDTO.marketingAgree = _isApproveList.value!!.all {it == true}

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
            }
            R.id.female -> {
                _isGenderList.value =  listOf(false, true, false)
            }
            R.id.others -> {
                _isGenderList.value =  listOf(false, false, true)
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
        Log.d("Sign","${birthdayText.value}")
        val birth =  if(birthdayText.value != null) birthdayText.value.toString().replace(".", "").toInt() else null
        val nickname = nicknameText.value.toString()
        val gender_bool = _isGenderList.value!!.mapIndexedNotNull { index, value ->
            if (value) index else 3
        }

        var gender = ""
        when (gender_bool[0]) {
            0 -> gender = "male"
            1 -> gender = "female"
            2 -> gender = "others"
        }

        viewModelScope.launch {
            createMemberUseCase(nickname, birth, gender, marketingAgree = 1).let {
                when (it) {
                    is MappingResult.Success<*> -> {
                        Log.d("Sign","회원가입 성공")
                        //val data = it.data as SignResponseDTO
                        // 성공시 complete 화면 띄우기
                        _isComplete.value = true

                    }
                    is MappingResult.Error -> {
                        // 회원가입 실패 -> 다시 누르게? 처음부터 다시?
                        Log.e("Sign","회원가입 실패")
                        _isComplete.value = false
                        _errorLiveData.value = it.message
                        /*
                        when (it.code) {
                            400 -> _errorLiveData.value = it.message
                            500 -> _errorLiveData.value = it.message
                        }
                         */

                    }
                }
            }

        }
    }

}