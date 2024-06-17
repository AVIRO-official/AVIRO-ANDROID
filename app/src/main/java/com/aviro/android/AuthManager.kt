package com.aviro.android

import com.aviro.android.presentation.entity.SignInfo

interface AuthManager {

    fun initialize()

    suspend fun autoSignIn() : Boolean

    suspend fun manualSignIn() : SignInfo?

    fun logout()

    fun withdraw()

}