package com.android.aviro.presentation.sign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySignBinding
import com.android.aviro.presentation.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import com.android.aviro.BuildConfig
import java.util.*


@AndroidEntryPoint
class Sign : BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {


    //val viewModel by viewModels<SignViewModel>()
    private val viewmodel: SignViewModel by viewModels() //뷰모델 의존성 주입

    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    // URL에 붙여서 애플 서버에 보낼 요청파라미터
    private val mAuthEndpoint = "https://appleid.apple.com/auth/authorize"
    private val mResponseType = "code%20id_token"
    private val mResponseMode = "form_post"
    private lateinit var mClientId: String
    private val mScope = "name%20email"
    private val mState = UUID.randomUUID().toString()
    private lateinit var mRedirectUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        webView = WebView(this)

        // 웹뷰 세팅
        val webViewSettings = webView.settings
        webViewSettings.javaScriptEnabled = true
        webViewSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        //CookieManager.getInstance().setAcceptCookie(true)
        webViewSettings.setSupportMultipleWindows(true)
        webViewSettings.allowContentAccess = true
        webViewSettings.allowFileAccessFromFileURLs = true
        webViewSettings.allowFileAccess = true
        webViewSettings.allowUniversalAccessFromFileURLs = true
        webViewSettings.javaScriptCanOpenWindowsAutomatically = true
        WebView.setWebContentsDebuggingEnabled(true)
        webViewSettings.domStorageEnabled = true
        webViewSettings.loadsImagesAutomatically = true
        webViewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)

        // 웹뷰 동작
        val url = createUrl()
        webView.loadUrl(url)
        webView.webViewClient = AppleWebViewClient()
        setContentView(webView)
         */

        //viewModel = ViewModelProvider(this, ViewModelFactory(CreateSocialAccountUseCase(), CreateUserUseCase())).get(SignViewModel::class.java)

        //binding = ActivitySignBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // 뷰모델 설정
        //sharedViewModel = SignViewModel()
        //sharedViewModel.lifecycleOwner = this@Sign

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction
            .replace(R.id.fragment_container_view, SignSocialFragment())
            .addToBackStack("SignSocialFragment") // 백 스택에 추가
            .commit()

    }


    // 로그인 정보가 담긴 인텐트를 받는 메소드
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let { uri ->
            parseUri(uri)
        }
    }

    private fun parseUri(url: Uri){
        val stateParam = url.getQueryParameter("state")
        val idTokenParam = url.getQueryParameter("id_token")
        val codeParam = url.getQueryParameter("code")

        if (idTokenParam != null){
            Log.d("idTokenParam","${idTokenParam}")
            Log.d("codeParam","${codeParam}")

        }
        //finish()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, SignNicknameFragment())
            .addToBackStack("SignNicknameFragment") // 백 스택에 추가
            .commit()

    }


    /* webview를 사용하게 되는 경우 */

    private fun createUrl(): String{
        mClientId = getString(R.string.apple_service_id) //getString(R.string.apple_service_id)
        mRedirectUrl = "${BuildConfig.SIGN_APPLE_REDIRECT_URL}" //getString(R.string.sign_apple_redirect_url)
        return (mAuthEndpoint
                + "?response_type=$mResponseType"
                + "&response_mode=$mResponseMode"
                + "&client_id=$mClientId"
                + "&scope=$mScope"
                + "&state=$mState"
                + "&redirect_uri=$mRedirectUrl")
    }


    inner class AppleWebViewClient: WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean
        {
            val url = request?.url

            if(url?.toString()?.contains("${BuildConfig.SIGN_APPLE_RESPONSE_URL}")==true)
            {
                val stateParam = url?.getQueryParameter("state")
                val idTokenParam = url?.getQueryParameter("id_token")
                val codeParam = url?.getQueryParameter("code")

                Log.d("idTokenParam","${idTokenParam}")
                Log.d("codeParam","${codeParam}")

                return true
            }
            //val intent= Intent(Intent.ACTION_VIEW,Uri.parse(url));
            //startActivity(intent)
            return false
        }

    }

}