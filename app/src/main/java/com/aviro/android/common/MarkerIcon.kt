package com.aviro.android.common

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.aviro.android.R
import com.naver.maps.map.overlay.OverlayImage


fun getMarkerIcon(category: String, allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean) : OverlayImage {
    return when (category) {
            "식당" -> {
                if(allVegan) {
                     OverlayImage.fromResource(R.drawable.marker_default_dish_all)
                } else if(someMenuVegan){
                     OverlayImage.fromResource(R.drawable.marker_default_dish_part)
                } else {
                     OverlayImage.fromResource(R.drawable.marker_default_dish_request)
                }
            }
            "카페" -> {
                if(allVegan) {
                     OverlayImage.fromResource(R.drawable.marker_default_cafe_all)
                } else if(someMenuVegan){
                     OverlayImage.fromResource(R.drawable.marker_default_cafe_part)
                } else {
                     OverlayImage.fromResource(R.drawable.marker_default_cafe_request)
                }
            }
            "빵집" -> {
                if(allVegan) {
                     OverlayImage.fromResource(R.drawable.marker_default_bread_all)
                } else if(someMenuVegan){
                     OverlayImage.fromResource(R.drawable.marker_default_bread_part)
                } else {
                     OverlayImage.fromResource(R.drawable.marker_default_bread_request)
                }
            }
            "술집" -> {
                if(allVegan) {
                     OverlayImage.fromResource(R.drawable.marker_default_alcohol_all)
                } else if(someMenuVegan){
                     OverlayImage.fromResource(R.drawable.marker_default_alcohol_part)
                } else {
                     OverlayImage.fromResource(R.drawable.marker_default_alcohol_request)
                }
            }
        else -> {
            // 조건에 맞지 않는 경우에는 원하는 작업을 수행하거나 예외를 던질 수 있습니다.
            // 여기에서는 예외를 던지는 것으로 처리합니다.
            //throw IllegalArgumentException("Invalid category: $category")
            OverlayImage.fromResource(R.drawable.marker_default_alcohol_request)
        }
        }
}

fun getSelectedMarkerIcon(category: String, allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean) : OverlayImage {
    return when (category) {
        "식당" -> {
            if (allVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_dish_all)
            } else if (someMenuVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_dish_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_dish_request)
            }
        }

        "카페" -> {
            if (allVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_cafe_all)
            } else if (someMenuVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_cafe_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_cafe_request)
            }
        }

        "빵집" -> {
            if (allVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_bread_all)
            } else if (someMenuVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_bread_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_bread_request)
            }
        }

        "술집" -> {
            if (allVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_alcohol_all)
            } else if (someMenuVegan) {
                OverlayImage.fromResource(R.drawable.marker_select_alcohol_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_alcohol_request)
            }
        }

        else -> OverlayImage.fromResource(R.drawable.marker_select_dish_request)
    }
}


fun getBookmarkMarkerIcon(allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean) : OverlayImage {
     if(allVegan) {
         return OverlayImage.fromResource(R.drawable.marker_bookmark_green)
            } else if(someMenuVegan){
         return OverlayImage.fromResource(R.drawable.marker_bookmark_orange)
            } else {
         return OverlayImage.fromResource(R.drawable.marker_bookmark_yellow)
            }
}

fun getVeganType(allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean) : String {
    if(allVegan) {
        return "green"
    }else if(someMenuVegan){
        return "orange"
    }else {
        return "yellow"
    }
}


fun setMarkerClickListener(category: String, veganType : String) : OverlayImage {
    return when (category) {
        "식당" -> {
            if(veganType == "green") {
                OverlayImage.fromResource(R.drawable.marker_select_dish_all)
            } else if(veganType == "orange") {
                OverlayImage.fromResource(R.drawable.marker_select_dish_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_dish_request)
            }

        }
        "카페" -> {
            if(veganType == "green") {
                OverlayImage.fromResource(R.drawable.marker_select_cafe_all)
            } else if(veganType == "orange") {
                OverlayImage.fromResource(R.drawable.marker_select_cafe_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_cafe_request)
            }
        }
        "빵집" -> {
            if(veganType == "green") {
                OverlayImage.fromResource(R.drawable.marker_select_bread_all)
            } else if(veganType == "orange") {
                OverlayImage.fromResource(R.drawable.marker_select_bread_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_bread_request)
            }
        }
        "술집" -> {
            if(veganType == "green") {
                OverlayImage.fromResource(R.drawable.marker_select_alcohol_all)
            } else if(veganType == "orange") {
                OverlayImage.fromResource(R.drawable.marker_select_alcohol_part)
            } else {
                OverlayImage.fromResource(R.drawable.marker_select_alcohol_request)
            }
        }
        else -> {
            // 조건에 맞지 않는 경우에는 원하는 작업을 수행하거나 예외를 던질 수 있습니다.
            // 여기에서는 예외를 던지는 것으로 처리합니다.
            //throw IllegalArgumentException("Invalid category: $category")
            OverlayImage.fromResource(R.drawable.marker_default_alcohol_request)
        }
    }
}

fun setBookmarkMarkerClickListener(veganType : String) : OverlayImage {
            if(veganType == "green") {
              return  OverlayImage.fromResource(R.drawable.marker_bookmark_select_green)
            } else if(veganType == "orange") {
                return OverlayImage.fromResource(R.drawable.marker_bookmark_select_orange)
            } else {
                return OverlayImage.fromResource(R.drawable.marker_bookmark_select_yellow)
            }

}

        /*
    * 식당:dish, 카페:cafe, 빵집:bread, 술집:alcohol
    * 모든메뉴:all, 일부:part, 요청:requset
    */


fun getMarkerPin(context : Context, category: String, allVegan : Boolean, someMenuVegan : Boolean, ifRequestVegan : Boolean) : Drawable? {


    return when (category) {
        "식당" -> {
            if (allVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_dish_all)
            } else if (someMenuVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_dish_part)
            } else {
                ContextCompat.getDrawable(context, R.drawable.marker_select_dish_request)
            }
        }
        "카페" -> {
            if (allVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_cafe_all)
            } else if (someMenuVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_cafe_part)
            } else {
                ContextCompat.getDrawable(context, R.drawable.marker_select_cafe_request)
            }
        }

        "빵집" -> {
            if (allVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_bread_all)
            } else if (someMenuVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_bread_part)
            } else {
                ContextCompat.getDrawable(context, R.drawable.marker_select_bread_request)
            }
        }

        "술집" -> {
            if (allVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_alcohol_all)
            } else if (someMenuVegan) {
                ContextCompat.getDrawable(context, R.drawable.marker_select_alcohol_part)
            } else {
                ContextCompat.getDrawable(context, R.drawable.marker_select_alcohol_request)
            }
        }

        else -> ContextCompat.getDrawable(context, R.drawable.marker_select_dish_request)


    }
}