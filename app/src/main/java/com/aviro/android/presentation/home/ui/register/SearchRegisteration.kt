package com.aviro.android.presentation.home.ui.register

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.databinding.FragmentSearchRegisterationBinding
import com.aviro.android.domain.entity.search.SearchedRestaurantItem
import com.aviro.android.presentation.aviro_dialog.AviroDialogUtils
import com.aviro.android.presentation.entity.SortingLocEntity
import com.aviro.android.presentation.search.SearchAdapter
import com.aviro.android.presentation.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRegisteration : FragmentActivity() {

    private lateinit var binding: FragmentSearchRegisterationBinding
    private val viewmodel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        val X = intent.getDoubleExtra("NaverMapOfX", 0.0) //이름으로 값 추출, 만약 없다면 가져올 default 값 설정
        val Y = intent.getDoubleExtra("NaverMapOfY",0.0)
        // 기본 정렬 기준
        val SortingLoc = SortingLocEntity(X.toString(), Y.toString() , "accuracy")
        viewmodel._SrotingLocation.value = SortingLoc
        Log.d("SortingLoc","${SortingLoc}")


        binding.searchRecyclerview.adapter = SearchAdapter(viewmodel)

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

        viewmodel.searchList.observe(this) {
            if (viewmodel.isNewKeyword == true) {
                binding.searchRecyclerview.removeAllViews() // 기존 검색 리스트 삭제
                (binding.searchRecyclerview.adapter as SearchAdapter).searchedList = it as MutableList<SearchedRestaurantItem>?

            }
            // 무한 스크롤
            else {
                val currentPosition = (binding.searchRecyclerview.adapter as SearchAdapter).itemCount // 현재 아이템 총 갯수
                // 리사이클러 어댑터의 아이텝 리스트에 추가
                (binding.searchRecyclerview.adapter as SearchAdapter).searchedList!!.addAll(it.slice(currentPosition..it.size-1))
                Log.d("BindingAdapter", "${(binding.searchRecyclerview.adapter as SearchAdapter).searchedList}")
                // 새로 들어온 아이템 홀더에서 바인딩 하도록 notify
                (binding.searchRecyclerview.adapter as SearchAdapter).notifyItemRangeInserted(
                    currentPosition, // 새로 삽입될 포지션
                    viewmodel.searchListSize  // 삽입된 아이템의 개수
                )
            }
        }




        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.searchbarCancleBtn.setOnClickListener {
            binding.EditTextSearchBar.text = null
        }


        viewmodel.selectedSearchedItem.observe(this) { item ->
            if (item.placeId != null) {
                // 이미 등록된 가게
                AviroDialogUtils.createOneDialog(
                   this,
                    "이미 등록된 가게예요",
                    "다른 유저가 이미 등록한 가게예요.\n" +
                            "홈 화면에서 검색해보세요.",
                    "확인"
                ).show()
            } else {
                // 어비로 서버에서 등록된 가게인지 한번 더 확인
                viewmodel.checkIsRegister(item.placeName, item.addressName, item.x.toDouble(), item.y.toDouble())
                viewmodel.isRegistered.observe(this) {

                    if(it) {
                        // 이미 등록된 가게
                        AviroDialogUtils.createOneDialog(
                            this,
                            "이미 등록된 가게예요",
                            "다른 유저가 이미 등록한 가게예요.\n" +
                                    "홈 화면에서 검색해보세요.",
                            "확인"
                        ).show()

                    } else {
                        // Intent에 데이터 추가
                        intent.putExtra("search_item", item)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }

        }

    }



}