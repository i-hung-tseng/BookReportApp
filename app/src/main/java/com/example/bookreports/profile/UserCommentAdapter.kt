package com.example.bookreports.profile


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreports.data.userprofile.Comment
import com.example.bookreports.databinding.ProfileCommentItemViewBinding

class UserCommentAdapter(private val onCLickListener: OnClickListener) : ListAdapter<Comment, UserCommentAdapter.myViewHolder>(DiffCallback) {

    class myViewHolder(val binding: ProfileCommentItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.comment = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(ProfileCommentItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{
        onCLickListener.onClick(item)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == oldItem.id
        }
    }
    class OnClickListener(val clickListener: (item: Comment) -> Unit){
        fun onClick(item: Comment) = clickListener(item)
    }

}