package com.aviro.android.common

import java.math.RoundingMode
import kotlin.math.*

object DistanceCalculator {

    fun distanceMyLocation(place_lat : Double , place_lng : Double, user_lat : Double, user_lng : Double) : String {
        // 현재내위치 - 가게 거리 구하기 (하버사인)

        /**
         * 구에서 두 점 사이의 거리를 구할 때 사용하는 공식 (최단거리)
         */
        val distance = (6371000 * acos(
            cos(compareRadians(place_lat)) //place_lat
            * cos(compareRadians(user_lat)) //user_lat
                    * cos(compareRadians(place_lng) - compareRadians(user_lng)) // user_lng place_lng
                    + sin(compareRadians(user_lat)) * sin(compareRadians(place_lat))
        )).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toString()

        return translateDistanceLevel(distance)

    }

    private fun compareRadians(degrees : Double) : Double {
        return Math.toRadians(degrees)
    }


    fun  translateDistanceLevel(distance : String) : String {
        // 글자수 4개 넘어가면 Km
        if(distance.length >= 4) {
            val new_distance = distance.subSequence(0, distance.length - 3).toString() +  "." + distance.subSequence(distance.length - 3, distance.length - 2).toString() + "KM"
            return new_distance
        }
        return distance + "m"
    }

}