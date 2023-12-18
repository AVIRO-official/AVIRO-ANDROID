package com.android.aviro.presentation

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
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

        } else {
            button.background = if (animateOnChange == true ) ContextCompat.getDrawable(button.context, R.drawable.ic_check_activate) else ContextCompat.getDrawable(button.context, R.drawable.ic_check_non)

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

}