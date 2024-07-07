package com.aviro.android.presentation

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.aviro.android.R
import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.domain.entity.key.GOOGLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.domain.entity.marker.MarkerOfMap
import com.aviro.android.domain.entity.search.VeganOptions
import com.aviro.android.presentation.entity.BoxIcon
import com.aviro.android.presentation.guide.GuidePagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.net.URL

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("app:animatedNickname")
    fun setAnimationNicknameEditText(editText: EditText, isNext: Boolean?) { //animateOnChange
        if (isNext == null) {
            editText.background = ContextCompat.getDrawable(editText.context, R.drawable.base_edittext_non)
        } else {
            if (!isNext) {
                editText.background = ContextCompat.getDrawable(editText.context, R.drawable.base_edittext_wrong)
                val shakeAnimation: Animation =
                    AnimationUtils.loadAnimation(editText.context, R.anim.wrong_shake)
                    editText.startAnimation(shakeAnimation)
            } else {
                editText.background = ContextCompat.getDrawable(editText.context, R.drawable.base_edittext_right)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:animated")
    fun setAnimation(view: View, isNext: Boolean) { //animateOnChange
        if (!isNext) {
            val shakeAnimation: Animation =
                AnimationUtils.loadAnimation(view.context, R.anim.wrong_shake)
            view.startAnimation(shakeAnimation)
        }
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(button: View, animateOnChange: Boolean) {
        if (button.id == R.id.approveBtnAll ) {
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.btn_sign_terms_approve_activate) else ContextCompat.getDrawable(button.context, R.drawable.btn_sign_terms_approve_non)
        } else if (button.id == R.id.nextBtn ) {
            button.background = if (animateOnChange == true) ContextCompat.getDrawable(
                button.context,
                R.drawable.base_roundsquare_cobalt_30//btn_next_activate
            ) else ContextCompat.getDrawable(button.context, R.drawable.base_roundsquare_gray6_30) //btn_next_non

        } else if (button.id == R.id.approveBtn1 || button.id == R.id.approveBtn2 || button.id == R.id.approveBtn3 ){
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.ic_check_activate) else ContextCompat.getDrawable(button.context, R.drawable.ic_check_non)

        } else if (button.id == R.id.male || button.id == R.id.female || button.id == R.id.others || button.id == R.id.timeUpdateBtn ) {
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.base_roundsquare_cobalt_30) else ContextCompat.getDrawable(button.context, R.drawable.base_roundsquare_gray6_30)

        } else if (button.id == R.id.editTextbirthday){
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.base_edittext_right) else ContextCompat.getDrawable(button.context, R.drawable.base_edittext_wrong)

        } else if (button.id == R.id.search_bar_left_icon){
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.ic_arrow_left) else ContextCompat.getDrawable(button.context, R.drawable.ic_search)

        }
    }
    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(button: View, type: String) {
        if (button.id == R.id.vegan_type_ImageView ) {
            button.background = if (type == "vegan") ContextCompat.getDrawable(button.context, R.drawable.ic_menu_tag_green) else ContextCompat.getDrawable(button.context, R.drawable.ic_menu_tag_yellow)
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view : FloatingActionButton, isFavorite: Boolean) { //FloatingActionButton
        if (view.id == R.id.favorites_floatingButton) {
            if (isFavorite) {
                val drawableResId = R.drawable.ic_floating_favorite_active
                view.setImageResource(drawableResId)
            } else {
                val drawableResId = R.drawable.ic_floating_favorite_default
                view.setImageResource(drawableResId)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:setBottomSheetLikeBtn")
    fun setBottomSheetLikeBtn(view : LinearLayout, isFavorite: Boolean) { //FloatingActionButton
        view.background = if (isFavorite) ContextCompat.getDrawable(view.context, R.drawable.ic_like_selected) else ContextCompat.getDrawable(view.context, R.drawable.ic_like_bottomsheet_non)

    }



    @JvmStatic
    @BindingAdapter("app:dynamicTint")
    fun setDynamicTint(view: FloatingActionButton, isFavorite: Boolean) {
        val color = ContextCompat.getColor(view.context, if (isFavorite) R.color.Cobalt else R.color.Gray1)
        view.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    @JvmStatic
    @BindingAdapter("app:tint")
    fun setTint(button: FloatingActionButton, isFavorite: Boolean) {
        if (button.id == R.id.favorites_floatingButton) {
            val colorResId = if (isFavorite) {
                R.color.Cobalt
            } else {
                R.color.Gray1
            }
            val color = ContextCompat.getColor(button.context, colorResId)
            button.foregroundTintList = ColorStateList.valueOf(color) //setBackgroundTintList
        }

    }


    @JvmStatic
    @BindingAdapter("app:bgBirthdayEditText")
    fun setBirthdayEditTextBG(edittext: EditText, state: String) {
        when(state) {
            "true" -> edittext.background = ContextCompat.getDrawable(edittext.context, R.drawable.base_edittext_right)
            "false" -> edittext.background = ContextCompat.getDrawable(edittext.context, R.drawable.base_edittext_wrong)
            "default" -> edittext.background = ContextCompat.getDrawable(edittext.context, R.drawable.base_roundsquare_gray6_30)
        }
    }

    @JvmStatic
    @BindingAdapter("android:textColor")
    fun setTextColor(text: TextView, isChanged: Boolean) {
        if(isChanged) {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Gray7))
        } else {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Gray3))
        }
    }


    @JvmStatic
    @BindingAdapter("app:textColorWR")
    fun setTextColorByString(text: TextView, state: String) {
        if (state == "false") {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Warn_Red))
        } else {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Gray3))
        }
    }

    @JvmStatic
    @BindingAdapter("app:textColor")
    fun setTextColorByBool(view: TextView, isValid: Boolean?) {
        val id = view.id
        if(id == R.id.nickname_notice) {
            val is_valid = isValid ?: true
            if (!is_valid) {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Warn_Red))
            } else {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray2))
            }
        } else if(id == R.id.changeTextView || id == R.id.nextTextView) {
            if (isValid == true) {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray7))
            } else {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray3))
            }
        }
        else if(id == R.id.timeUpdateBtn ) {
            if (isValid == true) {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray7))
            } else {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray2))
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:bgCategoryFilter")
    fun setCategoryFilterBG(view : View, isSelected: Boolean) { //FloatingActionButton
        if (view.id == R.id.filter_dish) {
            if (isSelected) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_on_dish)
            } else {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_off_dish)
            }
        }
        else if(view.id == R.id.filter_cafe) {
            if (isSelected) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_on_cafe)
            } else {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_off_cafe)
            }
        }
        else if(view.id == R.id.filter_bakery) {
            if (isSelected) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_on_bread)
            } else {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_off_bread)
            }
        }
        else {
            if (isSelected) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_on_bar)
            } else {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_filter_off_bar)
            }
        }

    }



    @JvmStatic
    @BindingAdapter("app:setDotDrawable")
    fun setDotDrawable(view: View, isDot: Boolean) {
        if (isDot) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.dot_guide_select)
        } else {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.dot_guide_non)
        }
    }

    @JvmStatic
    @BindingAdapter("app:setPageChangeListener")
    fun ViewPager2.setPageChangeListener(pageChangeListener: ViewPager2.OnPageChangeCallback) {
        registerOnPageChangeCallback(pageChangeListener)
    }


    @JvmStatic
    @BindingAdapter("app:adapter")
    fun adapter(view: ViewPager2, guideAdapter : GuidePagerAdapter) {
        view.adapter = guideAdapter

    }

    @JvmStatic
    @BindingAdapter("app:bgVeganType")
    fun setVeganTypeBG(view: View, isSelected : Boolean) {
        if (view.id == R.id.card_green) {
            view.background = if (isSelected == true ) ContextCompat.getDrawable(view.context, R.drawable.card_vegantype_green) else ContextCompat.getDrawable(view.context, R.drawable.card_vegantype_green_default)
        } else if (view.id == R.id.card_orange) {
            view.background = if (isSelected == true) ContextCompat.getDrawable(view.context, R.drawable.card_vegantype_orange) else ContextCompat.getDrawable(view.context, R.drawable.card_vegantype_orange_default)
        } else {
            view.background = if (isSelected == true) ContextCompat.getDrawable(view.context, R.drawable.card_vegantype_yellow) else ContextCompat.getDrawable(view.context, R.drawable.card_vegantype_yellow_default)

        }
    }

    @JvmStatic
    @BindingAdapter("app:bgVeganType2")
    fun setVeganTypeBG2(view: View, marker : MarkerOfMap?) {
        if(marker != null) {
            when (marker.category) {
                "식당" -> {
                    when (marker.veganTypeColor) {
                        "green" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_dish_all)
                        }
                        "orange" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_dish_part)
                        }
                        "yellow" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_dish_request)
                        }
                    }

                }
                "카페" -> {
                    when (marker.veganTypeColor) {
                        "green" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_cafe_all)
                        }
                        "orange" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_cafe_part)
                        }
                        "yellow" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_cafe_request)
                        }
                    }
                }
                "빵집" -> {
                    when (marker.veganTypeColor) {
                        "green" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_bread_all)
                        }
                        "orange" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_bread_part)
                        }
                        "yellow" -> {
                            view.background = ContextCompat.getDrawable(
                                view.context,
                                R.drawable.box_bread_request
                            )
                        }
                    }
                }
                "술집" -> {
                    when (marker.veganTypeColor) {
                        "green" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_alcohol_all)
                        }
                        "orange" -> {
                            view.background =
                                ContextCompat.getDrawable(view.context, R.drawable.box_alcohol_part)
                        }
                        "yellow" -> {
                            view.background = ContextCompat.getDrawable(
                                view.context,
                                R.drawable.box_alcohol_request
                            )
                        }
                    }
                }
            }
        }

    }

    @JvmStatic
    @BindingAdapter("app:bgVeganType3")
    fun setVeganTypeBG3(view: View, type : BoxIcon) {
            when (type.category) {
                "식당" -> {
                    if(type.allVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_dish_all)
                    } else if(type.someMenuVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_dish_part)
                    } else {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_dish_request)
                    }
                }
                "카페" -> {
                    if(type.allVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_cafe_all)
                    } else if(type.someMenuVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_cafe_part)
                    } else {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_cafe_request)
                    }
                }
                "빵집" -> {
                    if(type.allVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_bread_all)
                    } else if(type.someMenuVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_bread_part)
                    } else {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_bread_request)
                    }
                }
                "술집" -> {
                    if(type.allVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_alcohol_all)
                    } else if(type.someMenuVegan) {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_alcohol_part)
                    } else {
                        view.background = ContextCompat.getDrawable(view.context, R.drawable.box_alcohol_request)
                    }
                }
            }

    }

    @JvmStatic
    @BindingAdapter("app:bgVeganType4")
    fun setVeganTypeBG4(view: View, veganType : BoxIcon) {
        if(veganType.allVegan) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_green)
        } else if(veganType.someMenuVegan) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_orange)
        } else {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_yellow)
        }
    }


    @JvmStatic
    @BindingAdapter("app:bgCheckBox")
    fun setCheckBoxBG(view: View, isSelected : Boolean) {
        if (view.id == R.id.requestBox) {
            view.background = if (isSelected == true ) ContextCompat.getDrawable(view.context, R.drawable.checkbox_yellow) else ContextCompat.getDrawable(view.context, R.drawable.checkbox_non)

    } else {
            view.background = if (isSelected == true ) ContextCompat.getDrawable(view.context, R.drawable.checkbox_blue) else ContextCompat.getDrawable(view.context, R.drawable.checkbox_non)
        }

    }


    @JvmStatic
    @BindingAdapter("app:placeVeganTypeIcon")
    fun setVeganTypeIconBG(view: View, type : VeganOptions) {
       if(type.allVegan == true) {
           view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_green)
       } else if(type.someMenuVegan == true) {
           view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_orange)
       } else if(type.ifRequestVegan == true) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_yellow)
       } else {
           view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_default)
       }


    }


    @JvmStatic
    @BindingAdapter("app:visibilityChanged")
    fun setVisibilityChanged(view: View, isVisible : Boolean) {
        view.visibility = if (isVisible == true) VISIBLE else GONE

    }

    @JvmStatic
    @BindingAdapter("app:registerBtnColor")
    fun setregisterBtnColor(view: TextView, isNext : Boolean) {
        if (isNext) {
            view.setTextColor(ContextCompat.getColor(view.context,R.color.Cobalt))
        } else {
            view.setTextColor(ContextCompat.getColor(view.context,R.color.Gray3))

        }
    }

    @JvmStatic
    @BindingAdapter("app:setEditText")
    fun setEditTextBG(view: EditText, isSelected : Boolean) {
        if (isSelected) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_gray6)
            view.setHintTextColor(ContextCompat.getColor(view.context, R.color.Gray3))
            view.isEnabled = true
        } else {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_gray5)
            view.setHintTextColor(ContextCompat.getColor(view.context, R.color.Gray0))
            view.isEnabled = false

        }

    }

    @JvmStatic
    @BindingAdapter("app:setTimetablePickerBG")
    fun setTimetablePickerBG(view : View, isValid : Boolean) {
        view.background = if(isValid) ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_border_gray7_10) else ContextCompat.getDrawable(view.context, R.drawable.base_roundsquare_border_gray6_10)
    }
    @JvmStatic
    @BindingAdapter("app:setTimetablePickerText")
    fun setTimetablePickerText(view : TextView, isValid : Boolean) {
        if(isValid) {
            view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray0))
        } else {
            view.setTextColor(ContextCompat.getColor(view.context, R.color.Gray3))
        }
    }


    @JvmStatic
    @BindingAdapter("app:veganTypeColor")
    fun setveganTypeColor(view : TextView, veganTypeColor : String?) {
        if(veganTypeColor != null) {
            when (veganTypeColor) {
                "green" -> view.setTextColor(ContextCompat.getColor(view.context, R.color.Green))
                "orange" -> view.setTextColor(ContextCompat.getColor(view.context, R.color.Orange))
                "yellow" -> view.setTextColor(ContextCompat.getColor(view.context, R.color.Yellow))
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:veganTypeIcon")
    fun setveganTypeIcon(view : View, veganTypeColor : String){
            when (veganTypeColor) {
                "green" -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_green)
                "orange" -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_orange)
                "yellow" -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_pin_type_yellow)
            }
    }


    /*
    @JvmStatic
    @BindingAdapter("app:items")
    fun setList(recyclerView: RecyclerView, items: ItemAdapter?) { // items는 모든 아이템
        items?.let { // items 이 null이 아니면
            // 검색 새로 함
            if (items.isNewKeyword == true) {
                recyclerView.removeAllViews() // 기존 검색 리스트 삭제
                (recyclerView.adapter as SearchAdapter).searchedList = items.itemList as MutableList<SearchedRestaurantItem>

            }
            // 무한 스크롤
            else {
                val currentPosition = (recyclerView.adapter as SearchAdapter).itemCount // 현재 아이템 총 갯수
                // 리사이클러 어댑터의 아이텝 리스트에 추가
                (recyclerView.adapter as SearchAdapter).searchedList!!.addAll(items.itemList.slice(currentPosition..items.itemList.size-1))
                Log.d("BindingAdapter", "${(recyclerView.adapter as SearchAdapter).searchedList}")
                // 새로 들어온 아이템 홀더에서 바인딩 하도록 notify
                (recyclerView.adapter as SearchAdapter).notifyItemRangeInserted(
                    currentPosition, // 새로 삽입될 포지션
                    items.addingCount // 삽입된 아이템의 개수
                )
            }
        }
    }

     */

    @JvmStatic
    @BindingAdapter("app:imageUrl")
    fun setImage(view: View, image_url : URL?) {
        image_url?.let {
            Glide.with(view.context).load(image_url).into(view as ImageView)
        }
    }

    @JvmStatic
    @BindingAdapter("app:timetableDayColor")
    fun setTimetableDayColor(text: TextView, isToday : Boolean) {
        if(isToday) {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Cobalt))
            text.typeface = Typeface.createFromAsset(text.context.assets, "pretendard_semibold.otf")

            //text.setTypeface(Typeface.createFromAsset(text.context.assets, "font/pretendard_semibold"))

        } else {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Gray0))
            text.typeface = Typeface.createFromAsset(text.context.assets, "pretendard_medium.otf")

            //text.setTypeface(Typeface.createFromAsset(text.context.assets, "font/pretendard_medium"))
        }
    }

    @JvmStatic
    @BindingAdapter("app:timetableOpenColor")
    fun setTimetableOpenColor(text: TextView, isToday : Boolean) {
        if(isToday) {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Cobalt))
            text.typeface = Typeface.createFromAsset(text.context.assets, "pretendard_semibold.otf")

            //text.setTypeface(Typeface.createFromAsset(text.context.assets, "font/pretendard_semibold.otf"))

        } else {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Gray3))
            text.typeface = Typeface.createFromAsset(text.context.assets, "pretendard_medium.otf")

            //text.setTypeface(Typeface.createFromAsset(text.context.assets, "font/pretendard_medium.otf"))
        }
    }

    @JvmStatic
    @BindingAdapter("app:timetableBreakColor")
    fun setTimetableBreakColor(text: TextView, isToday : Boolean) {
        if(isToday) {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Cobalt))
            text.typeface = Typeface.createFromAsset(text.context.assets, "pretendard_semibold.otf")

        } else {
            text.setTextColor(ContextCompat.getColor(text.context, R.color.Gray3))
            text.typeface = Typeface.createFromAsset(text.context.assets, "pretendard_medium.otf")

            //text.setTypeface(Typeface.createFromAsset(text.context.assets, "font/pretendard_medium.otf"))
        }
    }

    @JvmStatic
    @BindingAdapter("app:setSocialBG")
    fun setSocialSignBG(view: View, type: String) {
        when(type) {
            NAVER -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_sign_type_naver)
            KAKAO -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_sign_type_kakao)
            GOOGLE -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_sign_type_google)
            APPLE -> view.background = ContextCompat.getDrawable(view.context, R.drawable.ic_sign_type_apple)
        }

    }

}