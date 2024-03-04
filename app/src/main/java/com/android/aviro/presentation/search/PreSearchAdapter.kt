package com.android.aviro.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.aviro.databinding.SearchedWordBinding
import com.android.aviro.databinding.SearchedWordItemBinding
import com.android.aviro.domain.entity.SearchedRestaurantItem

class PreSearchAdapter(private val searchViewModel : SearchViewModel): RecyclerView.Adapter<PreSearchAdapter.PreSearchViewHolder>() {
    var preSearchedList: MutableList<SearchedRestaurantItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreSearchViewHolder {
        return PreSearchViewHolder.from(parent, searchViewModel)
    }

    override fun onBindViewHolder(holder: PreSearchViewHolder, position: Int) {
        if (preSearchedList != null) {
            holder.bind(preSearchedList!![position])
        }

    }

    override fun getItemCount() = preSearchedList?.size ?: 0


    // 뷰홀더 클래스
    class PreSearchViewHolder private constructor(
        val binding: SearchedWordItemBinding,
        val searchViewModel: SearchViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchedRestaurantItem) {
            binding.searchItem = item
            binding.viewmodel = searchViewModel

        }

        companion object {
            fun from(parent: ViewGroup, searchViewModel: SearchViewModel): PreSearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchedWordItemBinding.inflate(layoutInflater, parent, false)

                return PreSearchViewHolder(binding, searchViewModel)
            }
        }
    }



}