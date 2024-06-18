package com.aviro.android.presentation.sign

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aviro.android.AviroApplication
import com.aviro.android.BuildConfig
import com.aviro.android.R
import com.aviro.android.databinding.FragmentSignSocialBinding
import com.aviro.android.domain.entity.key.GOOGLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
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
            onClickGoogle()

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
        mRedirectUrl = "${BuildConfig.SIGN_APPLE_REDIRECT_URL}"

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

                viewmodel._signUserId.value = userId
                viewmodel._signType.value = NAVER
                viewmodel._signName.value = userName ?: ""
                viewmodel._signEmail.value = email ?: ""

                viewmodel.isSignUp()
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
                        NidOAuthLogin().callProfileApi(profileCallback)
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

        // 카카오계정으로 로그인
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
                        val email =  user.kakaoAccount?.email

                        viewmodel._signUserId.value = userId
                        viewmodel._signType.value = KAKAO
                        viewmodel._signName.value = userName
                        viewmodel._signEmail.value = email

                        viewmodel.isSignUp()
                    }
                }
            }
        }

        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)

    }

    fun onClickGoogle() {
        val signInIntent = GoogleSignInManager.getClient().signInIntent
        startActivityForResult(signInIntent, R.string.GOOGLE_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == R.string.GOOGLE_REQ_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // 구글 로그인 성공
                val account = task.getResult(ApiException::class.java)
                if (account != null && account.id != null && account.email != null) {

                    val userId = account.id
                    val userName =  account.familyName + account.givenName ?: ""
                    val userEmail = account.email

                    viewmodel._signType.value = GOOGLE
                    viewmodel._signUserId.value = userId
                    viewmodel._signName.value = userName
                    viewmodel._signEmail.value = userEmail

                    // 실험용
                    viewmodel.isSignUp()
                    //viewmodel.sucessGoogle(userId!!, userName, userEmail!!, GOOGLE, "구글로그인 실험용")

                }

            } catch (e: ApiException) {
                Log.d("GOOGLE:ApiException", "handleSignInResult: error" + e.statusCode)
                if (e.statusCode == 12501) {
                    // 사용자 취소
                }
            }
        }

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
