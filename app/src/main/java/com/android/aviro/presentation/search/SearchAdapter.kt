package com.android.aviro.presentation.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.aviro.data.entity.restaurant.SearchEntity
import com.android.aviro.databinding.SearchRestaurantItemBinding

class SearchAdapter(): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() { //val items : List<SearchEntity>

    lateinit var searchedList : List<SearchEntity> //items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchedList[position])
    }
    override fun getItemCount() = searchedList.size

    // 뷰홀더 클래스
    class SearchViewHolder private constructor(val binding: SearchRestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind( item: SearchEntity) {
            Log.d("SearchViewHolder","${item}")
            binding.searchItem = item
            //binding.viewmodel = viewModel
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
