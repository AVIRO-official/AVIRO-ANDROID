package com.aviro.android.presentation.bottomsheet

fun getVeganTypeColor2Text(veganTypeColor : String) : String{
    when(veganTypeColor) {
        "green" -> {
            return "모든 메뉴가 비건"
        }
        "orange" -> {
            return "일부 메뉴가 비건"
        }
        "yellow" -> {
            return "비건 메뉴로 요청 가능"
        }

        else -> {
            throw IllegalArgumentException("Invalid veganTypeColor: $veganTypeColor")
        }
    }
}