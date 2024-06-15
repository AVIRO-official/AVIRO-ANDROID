package com.aviro.android.presentation.update

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.databinding.PublicAddressItemBinding
import com.aviro.android.domain.entity.search.PublicAddressItem


class PublicAddressAdapter(val onClickItem : (String) -> Unit): RecyclerView.Adapter<PublicAddressAdapter.PublicAddressViewHolder>() { //val items : List<SearchEntity>

    lateinit var viewmodel : UpdateAddressViewModel
    var addressList : MutableList<PublicAddressItem>? = null // 이게 변경되면 기존거 다 제거하고 반영하는건가?


    // 뷰홀더 클래스
    inner class PublicAddressViewHolder(val binding: PublicAddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PublicAddressItem) {
            binding.addressItem = item

            // 클릭시 주소 적용
            binding.itemContainer.setOnClickListener {
                onClickItem(item.roadAddr)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicAddressViewHolder {
        return PublicAddressViewHolder(PublicAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: PublicAddressViewHolder, position: Int) {
        if(addressList != null) {
            holder.bind(addressList!![position])
        }
    }


    override fun getItemCount() = addressList?.size ?: 0

    fun setViewModel(viewModel: UpdateAddressViewModel) {
        this.viewmodel = viewModel
    }

    fun setData() {

    }


  /*      companion object {
            fun from(parent: ViewGroup) : PublicAddressViewHoldder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PublicAddressItemBinding.inflate(layoutInflater, parent, false)

                return PublicAddressViewHoldder(binding)
            }
        }*/



}