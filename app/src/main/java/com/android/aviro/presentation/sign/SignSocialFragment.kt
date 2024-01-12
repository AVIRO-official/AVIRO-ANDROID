package com.android.aviro.presentation.sign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.aviro.BuildConfig
import com.android.aviro.R
import com.android.aviro.databinding.FragmentSignSocialBinding
import java.util.*


class SignSocialFragment : Fragment() {

    private val sharedViewModel: SignViewModel by activityViewModels()

    private val mAuthEndpoint = "https://appleid.apple.com/auth/authorize"
    private val mResponseType = "code%20id_token"
    private val mResponseMode = "form_post"
    private lateinit var mClientId: String
    private val mScope = "name%20email"
    private val mState = UUID.randomUUID().toString()
    private lateinit var mRedirectUrl : String

    private var _binding: FragmentSignSocialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignSocialBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.viewmodel = sharedViewModel
        binding.lifecycleOwner = this@SignSocialFragment


        //parent = requireActivity() as Sign
        binding.appleBtn.setOnClickListener {
            onClickApple()
        }


        return view
    }



    fun onClickApple() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
