package com.android.aviro.domain.entity.base


sealed class MappingResult  {

    data class Success<T>(
        val message : String?, // 성공시 메세지
        val data: T? // 성공시 꼭 필요한 데이터

    ) : MappingResult()

    data class Error(val message: String?) : MappingResult() // 실패시 메세지
}
