package com.aviro.android.domain.usecase.member

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateBookmarkUseCase @Inject constructor (
    private val getMyInfoUseCase : GetMyInfoUseCase,
    private val memberRepository : MemberRepository,
    ){

    suspend fun addBookmark(place_id : String) : MappingResult {
        val userId = getMyInfoUseCase.getUserId()
        when(userId) {
            is MappingResult.Success<*> -> {
                return memberRepository.addBookmark(place_id, userId.data.toString())
            }
            is MappingResult.Error -> {
                return userId
            }
        }
    }


    suspend fun deleteBookmark(place_id : String) : MappingResult{
        val userId = getMyInfoUseCase.getUserId()
        when(userId) {
            is MappingResult.Success<*> -> {
                return memberRepository.deleteBookmark(place_id, userId.data.toString())
            }
            is MappingResult.Error -> {
                return userId
            }
        }

    }

}