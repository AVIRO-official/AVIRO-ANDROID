package com.android.aviro.data.mapper

import com.android.aviro.data.model.auth.SignInRequest
import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.auth.TokenResponse
import com.android.aviro.data.model.auth.TokensRequest
import com.android.aviro.domain.entity.auth.Sign
import com.android.aviro.domain.entity.auth.Tokens


    fun toSiginInRequest(refresh_token : String) : SignInRequest {
        return SignInRequest(
            refreshToken = refresh_token
        )
    }

    fun toTokensRequest(id_token : String, auth_code : String) : TokensRequest {
        return TokensRequest(
            identityToken = id_token,
            authorizationCode = auth_code
        )
    }

    fun SignInResponse.toSignIn() : Sign {
        return Sign(
            userId = this.userId,
            userName = this.userName,
            userEmail = this.userEmail,
            nickname = this.nickname,
        )
    }

    fun TokenResponse.toTokens() : Tokens {
        return Tokens(
            isMember = this.isMember,
            userId = this.userId,
            refreshToken = this.refreshToken,
            accessToken = this.accessToken
        )
    }
