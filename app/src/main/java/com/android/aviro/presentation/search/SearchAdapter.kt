package com.android.aviro.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.aviro.databinding.SearchRestaurantItemBinding
import com.android.aviro.domain.entity.SearchedRestaurantItem

class SearchAdapter(private val searchViewModel : SearchViewModel): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() { //val items : List<SearchEntity>

    var searchedList : MutableList<SearchedRestaurantItem>? = null // 이게 변경되면 기존거 다 제거하고 반영하는건가?


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent, searchViewModel)
            /* return SearchViewHolder(
                 SearchRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                 this.searchedItemClickLister)*/ //SearchViewHolder.from(parent,searchedItemClickLister) // 가게 데이터 하나를 홀더에 셋팅해줄 때마다 그 가게 데이터에 대한 레이아웃을 뷰바인딩 해주고
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if(searchedList != null) {
            holder.bind(searchedList!![position])
        }

    }

    override fun getItemCount() = searchedList?.size ?: 0


    // 뷰홀더 클래스
     class SearchViewHolder private constructor(val binding: SearchRestaurantItemBinding, val searchViewModel : SearchViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchedRestaurantItem) {
            binding.searchItem = item // 가게 데이터 한 줄 레이아웃에 사용될 데이터 Entity 데이터바인딩
            //아이템의 정보 어비로 서버로 보내서 비건 유형 알아오기
            binding.viewmodel = searchViewModel

        }

        companion object {
            fun from(parent: ViewGroup, searchViewModel: SearchViewModel) : SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchRestaurantItemBinding.inflate(layoutInflater, parent, false)

                return SearchViewHolder(binding, searchViewModel)
            }
        }
    }


}
