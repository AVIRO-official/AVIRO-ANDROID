package com.aviro.android.presentation.bottomsheet

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aviro.android.databinding.MenuItemBinding
import com.aviro.android.domain.entity.menu.Menu


class MenuAdapter: RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){

    var menuList: MutableList<Menu>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        if (menuList != null) {
            Log.d("onBindViewHolder", "${menuList}")
            holder.bind(menuList!![position])
        }
    }
    override fun getItemCount() = menuList?.size ?: 0


    class MenuViewHolder private constructor(
        val binding: MenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Menu) {
            Log.d("MenuViewHolder", "${item}")
            binding.menu = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MenuViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MenuItemBinding.inflate(layoutInflater, parent, false)

                return MenuViewHolder(binding)
            }
        }
    }

}