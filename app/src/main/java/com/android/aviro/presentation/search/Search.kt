package com.android.aviro.presentation.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        binding.searchRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 다음페이지 존재 여부 확인
                if(viewmodel.isEnd == true) return;

                // 아직 로딩중인지 확인
                if (viewmodel._isProgress.value == false) { // 아직 로딩중이면 호출 x
                    // 스크롤이 끝에 도달했는지 확인
                    if (binding.searchRecyclerview.canScrollVertically(-1)) {
                        Log.d("searchRecyclerview", "스크롤 끝에 도달함!!")
                        viewmodel.currentPage++
                        viewmodel.nextList()
                    }
                }
            }

        })

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



