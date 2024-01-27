package com.android.aviro.presentation.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.android.aviro.R
import com.android.aviro.databinding.ActivitySearchBinding
import com.android.aviro.presentation.sign.SignViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Search : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewmodel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        binding.backBtn.setOnClickListener {
            //overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_right_enter)
            finish()
        }


        // 검색중 아님 -> 이전검색어 있는지 여부에 따라 다른 내용
        viewmodel.isPreSearched.observe(this, Observer {
            val addLayout = if(it) layoutInflater.inflate(R.layout.search_word, null) else layoutInflater.inflate(R.layout.search_tutorial, null)
            binding.includeLayout.addView(addLayout)

        })


        viewmodel.isSearching.observe(this, Observer {
            // 검색중 아니면 포커스 내려가게
            if(it) binding.EditTextSearchBar.requestFocus()  else binding.EditTextSearchBar.clearFocus()
            // 검색중이면 리스트 화면
            //val addLayout = if(it) layoutInflater.inflate(R.layout.searched_restaurant_item, null) else layoutInflater.inflate(R.layout.search_tutorial, null)
            //binding.includeLayout.addView(addLayout)
        })

    }
}