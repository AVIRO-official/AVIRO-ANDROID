package com.aviro.android.presentation.sign

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.R
import com.aviro.android.databinding.FragmentSignSocialBinding
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import java.util.*


class SignSocialFragment : Fragment() {

    private val viewmodel: SignViewModel by activityViewModels()


    private var _binding: FragmentSignSocialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignSocialBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this@SignSocialFragment



        binding.appleBtn.setOnClickListener {
            onClickApple()
        }

        binding.naverBtn.setOnClickListener {
            onClickNaver()
        }


        binding.kakaoBtn.setOnClickListener {
            onClickKaKao()
        }


        binding.googleBtn.setOnClickListener {
            onClickGoggle()
        }

        // 에러 팝업
        viewmodel.errorLiveData.observe(viewLifecycleOwner){
           it?.let { errorMsg ->
               AviroDialogUtils.createOneDialog(
                   requireContext(),
                   "Error",
                   errorMsg,
                   "확인"
               ).show()
            }
        }


        return view
    }

    fun onClickApple() {

         val mAuthEndpoint = "https://appleid.apple.com/auth/authorize"
         val mResponseType = "code%20id_token"
         val mResponseMode = "form_post"
        lateinit var mClientId: String
         val mScope = "name%20email"
         val mState = UUID.randomUUID().toString()
        lateinit var mRedirectUrl : String

        mClientId = getString(R.string.apple_service_id)
        mRedirectUrl = "${com.aviro.android.BuildConfig.SIGN_APPLE_REDIRECT_URL}"

        val uri = Uri.parse(mAuthEndpoint
                + "?response_type=$mResponseType"
                + "&response_mode=$mResponseMode"
                + "&client_id=$mClientId"
                + "&scope=$mScope"
                + "&state=$mState"
                + "&redirect_uri=$mRedirectUrl")

        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(requireContext(), uri)
    }


    fun onClickNaver() {

        // 네이버 계정 정보 가져오기
        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                val userName = response.profile?.name
                val email = response.profile?.email
                Log.d("AutoSignInUseCase:userId", "$userId")

                viewmodel._signUserId.value = userId
                viewmodel._signType.value = NAVER
                viewmodel._signName.value = userName
                viewmodel._signEmail.value = email

                viewmodel.isSignUp(NAVER, userId!!, email!!, userName!!)

            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 로컬에 토큰 저장
                when (NaverIdLoginSDK.getState().toString()) {
                    "OK" -> {
                        NidOAuthLogin().callProfileApi(profileCallback) // 사용자 정보 가져오기 (토큰을 로컬에 또 저장할 필요 X)
                    }
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

        }

        // 로그인 호출
        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)

    }


    fun onClickKaKao() {

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                AviroDialogUtils.createOneDialog(requireContext(),
                    "카카오 소셜로그인 에러",
                    "카카오계정으로 로그인할 수 없습니다.\n다시 시도하거나 다른 소셜로그인을 이용해주세요",
                    "확인")
            } else if (token != null) {
                Log.d("KAKAO", "카카오계정으로 로그인 성공 ${token.accessToken}")
                // 사용자 정보 받아옴
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        // 사용자 정보 가져오지 못하면 로그인 취소
                        UserApiClient.instance.logout {}
                    }
                    else if (user != null) {
                        val userId = user.id.toString()
                        val userName = user.kakaoAccount?.profile?.nickname
                        val email =  user.kakaoAccount?.email // 이름

                        viewmodel._signUserId.value = userId
                        viewmodel._signType.value = KAKAO
                        viewmodel._signName.value = userName
                        viewmodel._signEmail.value = email

                        viewmodel.isSignUp(KAKAO, userId, email!!, userName)
                    }
                }
            }
        }

        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        /*
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.d("KAKAO", "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    //if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    //    return@loginWithKakaoTalk
                    //}
                    AviroDialogUtils.createOneDialog(requireContext(),
                        "카카오 소셜로그인 에러",
                        "카카오톡으로 로그인할 수 없습니다.\n다시 시도하거나 다른 소셜로그인을 이용해주세요",
                        "확인")

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)

                } else if (token != null) {
                    Log.d("KAKAO", "카카오톡으로 로그인 성공 ${token.accessToken}")

                    // 사용자 정보 받아옴
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                           // 사용자 정보 가져오지 못하면 로그인 취소
                            UserApiClient.instance.logout {}
                        } else if (user != null) {
                            Log.d("KAKAO", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")

                            val userId = user.id.toString()
                            val userName = user.kakaoAccount?.profile?.nickname
                            val email =  user.kakaoAccount?.email

                            viewmodel._signUserId.value = userId
                            viewmodel._signType.value = KAKAO
                            viewmodel._signName.value = userName
                            viewmodel._signEmail.value = email

                            viewmodel.isSignUp(KAKAO, userId, email!!, userName ?: "")
                        }
                    }

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }

         */

    }

    fun onClickGoggle() {
        /*
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(
                Scope("https://www.googleapis.com/auth/userinfo.email"),
                Scope("https://www.googleapis.com/auth/userinfo.profile"),
                Scope("openid")
            )
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

         */
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
