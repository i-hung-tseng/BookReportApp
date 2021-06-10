package com.example.bookreports.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreports.data.book.detailbook.Comment
import com.example.bookreports.databinding.CommentItemViewBinding

class CommentAdapter: ListAdapter<Comment, CommentAdapter.myViewHolder>(DiffCallback) {


    class myViewHolder(val binding: CommentItemViewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Comment){
            binding.comment = item
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback: DiffUtil.ItemCallback<Comment>(){
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == oldItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
       return myViewHolder(CommentItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}