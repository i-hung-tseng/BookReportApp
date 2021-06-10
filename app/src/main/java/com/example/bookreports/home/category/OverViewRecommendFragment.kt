package com.example.bookreports.home.category

import android.annotation.SuppressLint
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
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentOverViewRecommendBinding
import com.example.bookreports.home.HomeFragmentDirections
import com.example.bookreports.utils.MainViewModel
import timber.log.Timber


class OverViewRecommendFragment : Fragment() {

    lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var recommendAdapter: RecommendAdapter


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding = FragmentOverViewRecommendBinding.inflate(inflater)


        binding.lifecycleOwner = this
        viewModel.recommendBooks.observe(viewLifecycleOwner, Observer {
            recommendAdapter.submitList(it)
        })
        var mRecyclerview = binding.recyclerviewRecommend










        viewModel.resetFirstComment()
        viewModel.addAlreadyComment(null)

        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerview.layoutManager = linearLayoutManager
        linearLayoutManager.setSmoothScrollbarEnabled(false)


        recommendAdapter = RecommendAdapter(RecommendAdapter.OnClickListener {
            viewModel.addDetailBook(it)
            viewModel.getSelectedBookDetailFromApi(it.id)
            Log.d("Testing","in recommadapter it: ${it.id}")
            if (this.findNavController().currentDestination?.id == R.id.homeFragment){
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
            }else{
                Log.d("Testing","不在當前 current")
            }
        })

        mRecyclerview.adapter = recommendAdapter

        val searchView = binding.searchBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchByName(query)
                    if (this@OverViewRecommendFragment.findNavController().currentDestination?.id == R.id.homeFragment) {
                        this@OverViewRecommendFragment.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToResultListFragment())
                    } else {
                        Timber.d("Not currentDirections")
                    }
                } else {
                    Toast.makeText(requireActivity(), "請確認有輸入", Toast.LENGTH_SHORT).show()
                }

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }


        })



        return binding.root
    }
}