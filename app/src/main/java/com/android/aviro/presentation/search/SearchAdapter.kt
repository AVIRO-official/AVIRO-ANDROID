package com.android.aviro.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.aviro.databinding.SearchRestaurantItemBinding
import com.android.aviro.domain.entity.SearchedRestaurantItem

class SearchAdapter(): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() { //val items : List<SearchEntity>

    var searchedList : MutableList<SearchedRestaurantItem>? = null // 이게 변경되면 기존거 다 제거하고 반영하는건가?


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent) // 가게 데이터 하나를 홀더에 셋팅해줄 때마다 그 가게 데이터에 대한 레이아웃을 뷰바인딩 해주고
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if(searchedList != null) {
            holder.bind(searchedList!![position])
        }
        // 가게 데이터 하나를 홀더에 셋팅해줄 때마다 그 레이아웃에 사용될 데이터 Entity가 있다면 그걸 데이터바인딩 해준다

    }
    override fun getItemCount() = searchedList?.size ?: 0

    fun addItem() {


    }

    // 뷰홀더 클래스
    class SearchViewHolder private constructor(val binding: SearchRestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchedRestaurantItem) {
            binding.searchItem = item // 가게 데이터 한 줄 레이아웃에 사용될 데이터 Entity 데이터바인딩
            //아이템의 정보 어비로 서버로 보내서 비건 유형 알아오기
            //binding.searchItem.x
            // 알아와서 아이콘 설정 해주기

            binding.item.setOnClickListener {
               // 클릭한 아이템 위치로 맵 화면 넘어가기
            }

            //binding.viewmodel = viewModel  // 뷰모델 데이터 바이딩
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchRestaurantItemBinding.inflate(layoutInflater, parent, false)

                return SearchViewHolder(binding)
            }
        }

    }
}
