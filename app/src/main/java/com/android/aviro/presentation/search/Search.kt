package com.android.aviro.presentation.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.aviro.data.entity.restaurant.SearchEntity
import com.android.aviro.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Search : AppCompatActivity() {

    private lateinit var binding:ActivitySearchBinding

    private val viewmodel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        binding.searchRecyclerview.adapter = SearchAdapter()

        binding.backBtn.setOnClickListener {
           finish()
        }


    }
}




// 검색중 아님 -> 이전검색어 있는지 여부에 따라 다른 내용
/*
viewmodel.isPreSearched.observe(this, Observer {
    val addLayout = if(it) layoutInflater.inflate(R.layout.search_word, null) else layoutInflater.inflate(R.layout.search_tutorial, null)
    //addLayout.viewmodel = viewmodel
    binding.includeLayout.addView(addLayout)

    // 이전검색어 모두 삭제

    // 새로운 검색어 추가될때마다 업데이트
})
 */



