package com.android.aviro.domain.usecase.user

import javax.inject.Inject

// 사용자 생성
class CreateUserUseCase @Inject constructor ()
{

    // 기존 회원 아닌 경우 회원가입 수행
    fun createUser() {

    }

    // 기존 회원인 경우 정보 가져옴
    fun getUserInfo() : Boolean{
        // refresh token 사용해서 정보 가져옴
        /*
        if(==400) { // 토큰 만료 및 회원x

           return false

        } else {
          //사용자 정보 저장
          return true
        }

         */
        return false
    }

}