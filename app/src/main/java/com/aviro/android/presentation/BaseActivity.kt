package com.aviro.android.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity

abstract class BaseActivity<V : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : FragmentActivity() {

    protected lateinit var binding : V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}