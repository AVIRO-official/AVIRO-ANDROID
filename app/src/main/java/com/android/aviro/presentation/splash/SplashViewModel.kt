package com.android.aviro.presentation.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import com.android.aviro.domain.usecase.auth.AutoSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor (
    private val autoSignInUseCase: AutoSignInUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {


    // 최초 앱실행인지 확인 -> sharedPref
    fun isFirstStartApp() : Boolean {
        val prefs = context.getSharedPreferences("first_visitor",MODE_PRIVATE)
        val firstRun = prefs.getBoolean("firstRun", true) // 처음엔 default 값 출력

        if (firstRun) {
            // 처음 실행될 때의 작업 수행
            // "처음 실행 여부"를 false로 변경
            prefs.edit().putBoolean("firstRun", false).apply()
        }
        return firstRun
    }



    // 자동로그인
    fun isSignIn(): Boolean {
        var isSign = false
        viewModelScope.launch() {
            // 로그인 성공 여부 반환
            isSign = autoSignInUseCase.invoke()

        }

        return isSign

    }


}