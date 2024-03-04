package com.android.aviro.presentation.home.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySearchBinding
import com.android.aviro.databinding.FragmentSearchRegisterationBinding
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.presentation.home.ui.map.MapViewModel
import com.android.aviro.presentation.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRegisteration : AppCompatActivity() {

    private lateinit var binding: FragmentSearchRegisterationBinding
    private val viewmodel: SearchViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        binding.backBtn.setOnClickListener {
            finish()
        }


        viewmodel.selectedSearchedItem.observe(this, ) {
            intent.putExtra("search_item",it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }



}