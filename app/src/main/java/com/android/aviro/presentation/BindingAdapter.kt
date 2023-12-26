package com.android.aviro.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.android.aviro.R
import com.android.aviro.presentation.guide.Guide
import com.android.aviro.presentation.guide.GuideMenuFragment
import com.android.aviro.presentation.guide.GuidePagerAdapter
import org.jetbrains.annotations.Nullable

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("app:animated")
    fun setAnimation(editText: EditText, isNext: Boolean?) { //animateOnChange
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
    @BindingAdapter("android:background")
    fun setBackground(button: View, animateOnChange: Boolean) {
        if (button.id == R.id.approveBtnAll ) {
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.btn_sign_terms_approve_activate) else ContextCompat.getDrawable(button.context, R.drawable.btn_sign_terms_approve_non)
        } else if (button.id == R.id.nextBtn ) {
            button.background = if (animateOnChange == true) ContextCompat.getDrawable(
                button.context,
                R.drawable.btn_next_activate
            ) else ContextCompat.getDrawable(button.context, R.drawable.btn_next_non)

        } else if (button.id == R.id.terms1 || button.id == R.id.terms2 || button.id == R.id.terms3 ){
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.ic_check_activate) else ContextCompat.getDrawable(button.context, R.drawable.ic_check_non)

        } else if (button.id == R.id.male || button.id == R.id.female || button.id == R.id.others ) {
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.base_edittext_round_square_activate) else ContextCompat.getDrawable(button.context, R.drawable.base_edittext_roundsquare_default)

        } else if (button.id == R.id.editTextbirthday){
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.base_edittext_right) else ContextCompat.getDrawable(button.context, R.drawable.base_edittext_wrong)

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
    @BindingAdapter("app:bgCheckBox")
    fun setCheckBoxBG(view: View, isSelected : Boolean) {
        if (view.id == R.id.requestBox) {
            view.background = if (isSelected == true ) ContextCompat.getDrawable(view.context, R.drawable.checkbox_yellow) else ContextCompat.getDrawable(view.context, R.drawable.checkbox_non)

    } else {
            view.background = if (isSelected == true ) ContextCompat.getDrawable(view.context, R.drawable.checkbox_blue) else ContextCompat.getDrawable(view.context, R.drawable.checkbox_non)
        }

    }

    @JvmStatic
    @BindingAdapter("app:visibilityChanged")
    fun setVisibilityChanged(view: View, isVisible : Boolean) {
        view.visibility = if (isVisible == true ) VISIBLE else GONE

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




}