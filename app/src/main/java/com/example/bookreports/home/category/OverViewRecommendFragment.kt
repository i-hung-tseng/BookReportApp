package com.example.bookreports.home.category

import android.accounts.Account
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.text.set
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.bookreports.R
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.databinding.FragmentOverViewRecommendBinding
import com.example.bookreports.home.BookViewModel
import com.example.bookreports.home.HomeFragmentDirections
import com.example.bookreports.registerinfo.AccountViewModel
import com.example.bookreports.utils.MainViewModel
import com.example.bookreports.utils.MyApp
import com.example.bookreports.utils.ScaleCenterItemLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class OverViewRecommendFragment : Fragment() {



    lateinit var linearLayoutManager: LinearLayoutManager
//    private val viewModel: MainViewModel by activityViewModels()
    lateinit var recommendAdapter: RecommendAdapter
    lateinit var binding: FragmentOverViewRecommendBinding
    private val vmBook: BookViewModel by sharedViewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentOverViewRecommendBinding.inflate(inflater)


        initBindiing()
//        initSearchBar()

        val nav = this.findNavController()

        val bitmap = BitmapFactory.decodeResource(context?.resources,R.drawable.vector)
       Timber.d("bitmap : $bitmap")
        Glide.with(binding.imageTest)
                .load(bitmap)
                .apply { RequestOptions.bitmapTransform(CircleCrop()) }
                .into(binding.imageTest)



        if (vmBook.recommendBooks.value == null || vmBook.recommendBooks.value == listOf<BookItem>()){
            vmBook.getBooksFromApi()

        }


        //嘗試失敗給予空的 list 但是沒網路不會觸發
        vmBook.recommendBooks.observe(viewLifecycleOwner, Observer {
            if (it == listOf<BookItem>()){
                Toast.makeText(requireActivity(),"加載圖片失敗，請稍後再嘗試",Toast.LENGTH_SHORT).show()

            }
        })



        vmBook.bookResult.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                if (nav.currentDestination?.id == R.id.homeFragment){
                    nav.navigate(R.id.resultListFragment)
                }
            }else{
                Toast.makeText(requireActivity(),"查無關鍵書籍",Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun initBindiing(){
        binding.lifecycleOwner = this
        binding.bookViewModel = vmBook

        val nav = this.findNavController()
        var mRecyclerview = binding.recyclerviewRecommend
        linearLayoutManager = ScaleCenterItemLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        linearLayoutManager = LinearLayoutManager(requireContext())
//        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerview.layoutManager = linearLayoutManager
        linearLayoutManager.setSmoothScrollbarEnabled(false)
        recommendAdapter = RecommendAdapter(RecommendAdapter.OnClickListener {
            vmBook.getSelectedBookDetailFromApi(it.id)
            if (nav.currentDestination?.id == R.id.homeFragment){
                Timber.d("enterOverViewRecommendFragmetn")
                nav.navigate(R.id.detailFragment)
            }
        })


        mRecyclerview.adapter = recommendAdapter

    }
//    private fun initSearchBar(){
//
//        val searchView = binding.searchBar
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//                    vmBook.searchByBookNameFromApi(query)
//                    Timber.d("進到 searchView query: $query")
//                } else {
//                    Toast.makeText(requireActivity(), "請確認有輸入", Toast.LENGTH_SHORT).show()
//                }
//
//                return true
//
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//
//
//        })
//    }
}