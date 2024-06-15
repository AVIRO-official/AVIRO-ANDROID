package com.aviro.android.presentation.search

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.databinding.ActivitySearchBinding
import com.aviro.android.domain.entity.search.SearchedRestaurantItem
import com.aviro.android.presentation.aviro_dialog.SortingAccDisDialog
import com.aviro.android.presentation.aviro_dialog.SortingLocationDialog
import com.aviro.android.presentation.entity.SortingLocEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Search : FragmentActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewmodel: SearchViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        setLocation()
        setAdapter()
        initObserver()
        initListener()

    }

    fun setLocation()
    {
        val X = intent.getDoubleExtra("NaverMapOfX", 0.0)
        val Y = intent.getDoubleExtra("NaverMapOfY", 0.0)

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
    }

    fun setAdapter()
    {
        binding.PreSearchRecyclerview.adapter = PreSearchAdapter(viewmodel, binding.EditTextSearchBar)
        binding.searchRecyclerview.adapter = SearchAdapter(viewmodel)
    }
    fun initObserver() {

        viewmodel.preSearchedItems.observe(this, ) {
            (binding.PreSearchRecyclerview.adapter as PreSearchAdapter).preSearchedList =
                it?.toList()?.toMutableList()
            (binding.PreSearchRecyclerview.adapter as PreSearchAdapter).notifyDataSetChanged()
        }

        // 새로은 검색결과
        viewmodel.searchList.observe(this) {
            if (viewmodel.isNewKeyword == true) {
                binding.searchRecyclerview.removeAllViews() // 기존 검색 리스트 삭제
                (binding.searchRecyclerview.adapter as SearchAdapter).searchedList = it as MutableList<SearchedRestaurantItem>?

            }
            else {
                val currentPosition = (binding.searchRecyclerview.adapter as SearchAdapter).itemCount // 현재 아이템 총 갯수
                // 리사이클러 어댑터의 아이텝 리스트에 추가
                (binding.searchRecyclerview.adapter as SearchAdapter).searchedList!!.addAll(it.slice(currentPosition..it.size-1))
                (binding.searchRecyclerview.adapter as SearchAdapter).notifyItemRangeInserted(
                    currentPosition, // 새로 삽입될 포지션
                    viewmodel.searchListSize  // 삽입된 아이템의 개수
                )
            }
        }


        viewmodel.selectedSearchedItem.observe(this) {
            viewmodel.storeSearchedWord(it.placeName) // 가게 등록에는 필요 없음
            intent.putExtra("search_item", it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun initListener() {

        // 검색 위치 설정
        binding.locationSortBtn.setOnClickListener {
            SortingLocationDialog(this,  viewmodel._SortLocText.value!!, { viewmodel.sortCenterOfMapLoc() }, { viewmodel.sortCenterOfMyLoc() } ).show()
           /* AviroDialogUtils.createTwoChoiceDialog(this, "검색 위치 설정",
                "지도 중심", "내 위치 중심",
                { viewmodel.sortCenterOfMapLoc() }, { viewmodel.sortCenterOfMyLoc() }).show()

            */
        }

        //  정렬 기준 변경
        binding.accuracySortBtn.setOnClickListener {
            SortingAccDisDialog(this,  viewmodel._SortAccText.value!!, { viewmodel.sortAccuracy() }, { viewmodel.sortDistance() } ).show()
            /*
            AviroDialogUtils.createTwoChoiceDialog(this, "정렬 기준",
                "정확도순", "거리순",
                { viewmodel.sortAccuracy() }, { viewmodel.sortDistance() }).show()

             */
        }


        binding.searchbarCancleBtn.setOnClickListener {
            binding.EditTextSearchBar.text = null
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

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
                        viewmodel.currentPage++
                        viewmodel.nextList()
                    }
                }

            }
        })

    }
}





