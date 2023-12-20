package com.android.aviro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.aviro.domain.usecase.user.CreateSocialAccountUseCase
import com.android.aviro.domain.usecase.user.CreateUserUseCase
import com.android.aviro.presentation.sign.SignViewModel

class ViewModelFactory(val createSocialAccountUseCase: CreateSocialAccountUseCase,
                       val createUserUseCase: CreateUserUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignViewModel::class.java)) {
            SignViewModel(createSocialAccountUseCase,createUserUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}