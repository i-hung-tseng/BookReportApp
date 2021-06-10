package com.example.bookreports.resultlist


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.data.searchbook.SearchBook
import com.example.bookreports.databinding.ResultItemViewBinding

class ResultAdapter(private val onCLickListener: OnClickListener): ListAdapter<BookItem, ResultAdapter.myViewHolder>(DiffCallback) {



    class myViewHolder(val binding:ResultItemViewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:BookItem){
            binding.bookItem = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{
            onCLickListener.onClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(ResultItemViewBinding.inflate(LayoutInflater.from(parent.context)))

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