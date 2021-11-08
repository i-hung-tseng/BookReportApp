package com.example.bookreports.utils

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.bookreports.R
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.data.book.detailbook.Comment
import com.example.bookreports.detail.CommentAdapter
import com.example.bookreports.home.category.BusinessAdapter
import com.example.bookreports.home.category.RecommendAdapter
import com.example.bookreports.profile.UserCommentAdapter
import com.example.bookreports.resultlist.ResultAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.security.AccessController.getContext
import java.security.MessageDigest


@BindingAdapter("bind_itemView")
fun bindImageView(view: ImageView, url: String?) {


        Timber.d("enterBinding uri: $url" )
    if (url != null) {
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
    }else{
        Glide.with(view.context)
                .load(R.drawable.sana)
                .into(view)

    }
}



@BindingAdapter("result_list")
fun bindResultList(recyclerview:RecyclerView,data: List<BookItem>?){
    Timber.d("resultData : ${data?.size}")
    val adapter = recyclerview.adapter as ResultAdapter?
    adapter?.submitList(data)
}



@BindingAdapter("category_list")
fun bindCategoryList(recyclerview: RecyclerView,data: List<BookItem>?){
    val adapter = recyclerview.adapter as BusinessAdapter?
    adapter?.submitList(data)
}



@BindingAdapter("comment_list")
fun bindCommentAdapter(recyclerview: RecyclerView,data:List<Comment>?){
    val adapter = recyclerview.adapter as CommentAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindProfile")
fun bindProfile(view:ImageView,uri:String?){

    class RoundedCornerCenterCrop(val radius: Int = 0): BitmapTransformation(){
        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        }

        override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = TransformationUtils.centerCrop(pool,toTransform,outWidth,outHeight)
            return TransformationUtils.roundedCorners(pool,bitmap,radius)
        }
    }

    if(uri == null){

        Glide.with(view)
                .load(R.drawable.vector)
                .into(view)

    }else{
        Timber.d("進到 bindProfile")
        Glide.with(view)
                .load(uri)
                .into(view)
    }

}

@BindingAdapter("bindRecommendList")
fun bindRecommendList(recyclerview: RecyclerView, data: List<BookItem>?){
    Timber.d("enter bindAdapter data:$data")
    val adapter = recyclerview.adapter as RecommendAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindProfileComment")
fun bindProfileComment(recyclerview: RecyclerView, data: List<com.example.bookreports.data.userprofile.Comment>?){
    val adapter = recyclerview.adapter as UserCommentAdapter
    adapter.submitList(data)
}

