package com.aviro.android.presentation.sign

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

object GoogleSignInManager {

    private lateinit var googleSignInClient: GoogleSignInClient

    fun initialize(context: Context, clientId: String) {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(
                Scope("https://www.googleapis.com/auth/userinfo.email"),
                Scope("https://www.googleapis.com/auth/userinfo.profile"),
                Scope("openid"))
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getClient(): GoogleSignInClient {
        if (!::googleSignInClient.isInitialized) {
            throw IllegalStateException("GoogleSignInClient is not initialized")
        }
        return googleSignInClient
    }
}