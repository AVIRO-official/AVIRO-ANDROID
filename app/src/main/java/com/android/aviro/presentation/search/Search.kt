package com.android.aviro.presentation.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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

        val X = intent.getStringExtra("NaverMapOfX") //이름으로 값 추출, 만약 없다면 가져올 default 값 설정
        val Y = intent.getStringExtra("NaverMapOfY")
        viewmodel.centerOfMapX = X
        viewmodel.centerOfMapY = Y

        // 기본 정렬 기준

        if(viewmodel.getCurrentGPSLoc() != null){
            val location = viewmodel.getCurrentGPSLoc()
            val SortingLoc = SortingLocEntity(location!!.longitude.toString(),location.latitude.toString(), "accuracy")
            viewmodel._SrotingLocation.value = SortingLoc
        } else {
            val SortingLoc = SortingLocEntity(X.toString(), Y.toString(), "accuracy")
            viewmodel._SrotingLocation.value = SortingLoc
        }

        binding.searchRecyclerview.adapter = SearchAdapter()

        // 스크롤 발생할 때 호출
        binding.searchRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 다음페이지 존재 여부 확인
                if(viewmodel.isEnd == true) return

                // 아직 로딩중인지 확인
                if (viewmodel._isProgress.value == false) { // 아직 로딩중이면 호출 x
                    // 스크롤이 끝에 도달했는지 확인
                    if (!binding.searchRecyclerview.canScrollVertically(1)) { // 더이상 하단으로 내려갈 수 없음
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





