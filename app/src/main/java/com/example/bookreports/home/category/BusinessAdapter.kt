package com.example.bookreports.home.category


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreports.data.book.Book
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.databinding.CategoryItemViewBinding

class BusinessAdapter(private val onClickListener: OnClickListener): ListAdapter<BookItem, BusinessAdapter.myViewHolder>(DiffCallback) {


 class myViewHolder(val binding :CategoryItemViewBinding):RecyclerView.ViewHolder(binding.root) {
     fun bind(item: BookItem) {
        binding.itemview = item
        binding.executePendingBindings()
     }

 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(CategoryItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(book)
        }

    }

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