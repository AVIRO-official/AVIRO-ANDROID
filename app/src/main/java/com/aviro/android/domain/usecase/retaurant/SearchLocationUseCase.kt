package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.RestaurantRepository
import javax.inject.Inject

// 사용자가 주소를 수정하기 위해 검색
class SearchLocationUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(keyword : String, page : Int) : MappingResult {
        return restaurantRepository.searchPublicAddress(keyword, page)
    }

    suspend fun getCoordinationOfAddress(address : String) : MappingResult{
        return restaurantRepository.getCoordinationOfAddress(address)
    }

    suspend fun getRoadAddressOfCoordination(x : Double, y : Double) : MappingResult{
        return restaurantRepository.getAddressOfCoordination(x.toString(), y.toString())
    }
}