package com.example.bookreports.home.category

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.databinding.RecommendItemViewBinding

class RecommendAdapter(private val onClickListener: OnClickListener) : ListAdapter<BookItem,RecommendAdapter.viewHolder>(DiffCallback) {



    class viewHolder(val binding:RecommendItemViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BookItem){
                binding.bookItem = item
                binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(RecommendItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(book)
        }
    }


    // TODO: 2021/5/18 為什麼這邊需要判斷? 以及為什麼要加?
    companion object DiffCallback: DiffUtil.ItemCallback<BookItem>(){
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
                    return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
                    return oldItem.id == oldItem.id
        }
    }
    class OnClickListener(val clickListener: (item: BookItem) -> Unit){
        fun onClick(item: BookItem) = clickListener(item)
    }
}