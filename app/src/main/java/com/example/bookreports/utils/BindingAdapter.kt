package com.example.bookreports.utils

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.bookreports.R
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.detail.CommentAdapter
import com.example.bookreports.home.category.BusinessAdapter
import com.example.bookreports.resultlist.ResultAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.security.AccessController.getContext


@BindingAdapter("bind_itemView")
fun bindImageView(view: ImageView, url: String) {


    url?.let {
        Glide.with(view.context)
                .load(url)
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.ic_imagedownload)
                                .error(R.drawable.ic_imagefail)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                )
                .into(view)
    }
}



@BindingAdapter("result_list")
fun bindResultList(recyclerview:RecyclerView,data: List<BookItem>?){
    val adapter = recyclerview.adapter as ResultAdapter?
    adapter?.submitList(data)
}



@BindingAdapter("category_list")
fun bindCategoryList(recyclerview: RecyclerView,data: List<BookItem>?){
    val adapter = recyclerview.adapter as BusinessAdapter?
    adapter?.submitList(data)
}



@BindingAdapter("comment_list")
fun bindCommentAdapter(recyclerview: RecyclerView,data:List<com.example.bookreports.data.book.detailbook.Comment>?){
    val adapter = recyclerview.adapter as CommentAdapter
    adapter.submitList(data)
}