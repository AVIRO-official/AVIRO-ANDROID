package com.aviro.android.presentation.home.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.R
import com.aviro.android.databinding.MyRestaurantItemBinding
import com.aviro.android.domain.entity.member.MyRestaurant
import com.aviro.android.presentation.entity.BoxIcon

class MyRestaurantAdapter(val viewmodel : MypageViewModel, val onClick: (MyRestaurant)->(Unit)) : RecyclerView.Adapter<MyRestaurantAdapter.MyRestaurantViewHolder>() { //

    var restaurantList: MutableList<MyRestaurant>? = null

    inner class MyRestaurantViewHolder (val binding: MyRestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyRestaurant) {
            binding.myRestaurantItem = item
            binding.viewmodel = viewmodel

            if(item.menuCountExceptOne != 0) {
                binding.restaurantMenu.text = "${item.menu} 외 ${item.menuCountExceptOne}개 메뉴"
            } else {
                binding.restaurantMenu.text = item.menu
            }

            binding.likeBtn.background = ContextCompat.getDrawable(binding.likeBtn.context, R.drawable.ic_like_selected)
            binding.iconType = BoxIcon(item.category, item.allVegan, item.someMenuVegan, item.ifRequestVegan)
            
            binding.likeBtn.setOnClickListener {
                if(item.isLike) {
                    binding.likeBtn.background = ContextCompat.getDrawable(binding.likeBtn.context, R.drawable.ic_like_floating_non)
                } else {
                    binding.likeBtn.background = ContextCompat.getDrawable(binding.likeBtn.context, R.drawable.ic_like_selected)
                }

                //binding.myRestaurantItem = item.copy(isLike = !item.isLike)

                viewmodel.updateBookmark(item.placeId, item.isLike)
                item.isLike = !item.isLike
            }


            binding.contentContainer.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRestaurantViewHolder {
        return MyRestaurantViewHolder(MyRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) //MyRestaurantViewHolder.from(viewmodel, parent)
    }

    override fun onBindViewHolder(holder: MyRestaurantViewHolder, position: Int) {
        if (restaurantList != null) {
            holder.bind(restaurantList!![position])
        }
    }

    override fun getItemCount() = restaurantList?.size ?: 0

    fun setData(restaurantList: MutableList<MyRestaurant>){
        this.restaurantList= restaurantList
    }



}