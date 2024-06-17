package com.aviro.android.presentation.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.usecase.auth.AutoSignInUseCase
import com.aviro.android.domain.usecase.auth.CreateTokensUseCase
import com.aviro.android.domain.usecase.auth.GetTokenUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor (
    private val autoSignInUseCase: AutoSignInUseCase
) : ViewModel() {


    val _isSignIn = MutableLiveData<Boolean>()
    val isSignIn: LiveData<Boolean>
        get() = _isSignIn


    // 자동로그인
     fun isSignIn()  {
         // 그냥 요청
        viewModelScope.launch {
            // 현재 어떤 로그인 되어 있는지 확인
            autoSignInUseCase().let {
                when(it){
                    is MappingResult.Success<*> -> _isSignIn.value = true
                    is MappingResult.Error -> {
                        _isSignIn.value = false
                    }
                }
            }
            }
        }

     }


