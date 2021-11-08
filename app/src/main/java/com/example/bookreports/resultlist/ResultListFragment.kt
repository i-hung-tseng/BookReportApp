package com.example.bookreports.resultlist

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreports.R
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.databinding.FragmentResultListBinding
import com.example.bookreports.home.BookViewModel
import com.example.bookreports.home.category.RecommendAdapter
import com.example.bookreports.profile.ProfileFragmentDirections
import com.example.bookreports.utils.MainViewModel
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class ResultListFragment : Fragment() {



    lateinit var resultAdapter :ResultAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var linerLayoutManager: LinearLayoutManager
    private val vmBook: BookViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding = FragmentResultListBinding.inflate(inflater)



        binding.bookViewModel = vmBook
        binding.lifecycleOwner = this
//        vmBook.bookResult.observe(viewLifecycleOwner, Observer {
//            Timber.d("result : ${it.size}")
//            resultAdapter.submitList(it)
//        })


        // TODO: 2021/7/2 確定這邊的 livedata 時間問題 
            vmBook.bookResult.observe(viewLifecycleOwner, Observer {

                Timber.d("enter bookResult $it")
                val searchWord = "關鍵字:" + "${vmBook.searchBookName.value}"
                val resultNum = "共有:" + "${vmBook.bookResult.value?.size}" + "筆資料"

                if (it.isNotEmpty() ) {

                    Timber.d("bookResult isNotEmpty")
                    val span = SpannableString(searchWord)
                    span.setSpan(ForegroundColorSpan(Color.RED), 4, searchWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.tvSearchWord.text = span

                    val spanNum = SpannableString(resultNum)
                    span.setSpan(ForegroundColorSpan(Color.RED), 3, resultNum.length - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.tvResultNum.text = spanNum
                }else {
                    Timber.d("bookName == listOf()")
                    Toasty.info(requireActivity(), "查無關鍵字，請重新查詢", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (this.findNavController().currentDestination?.id == R.id.resultListFragment)
                            this.findNavController().navigate(ResultListFragmentDirections.actionResultListFragmentToHomeFragment())
                    },3000)

                }
            })













        mRecyclerView = binding.recyclerviewResult
        linerLayoutManager = LinearLayoutManager(requireContext())
        linerLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = linerLayoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL))

        resultAdapter = ResultAdapter(ResultAdapter.OnClickListener(){
            vmBook.getSelectedBookDetailFromApi(it.id)
            this.findNavController().navigate(ResultListFragmentDirections.actionResultListFragmentToDetailFragment())
        })
        mRecyclerView.adapter = resultAdapter

        return binding.root



    }

    override fun onDestroy() {
//        vmBook.resetResultBook()
        super.onDestroy()
    }
}