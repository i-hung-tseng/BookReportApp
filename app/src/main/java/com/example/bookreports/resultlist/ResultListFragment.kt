package com.example.bookreports.resultlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentResultListBinding
import com.example.bookreports.home.category.RecommendAdapter
import com.example.bookreports.utils.MainViewModel
import timber.log.Timber


class ResultListFragment : Fragment() {



    private val viewModel: MainViewModel by activityViewModels()
    lateinit var resultAdapter :ResultAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var linerLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding = FragmentResultListBinding.inflate(inflater)




        binding.lifecycleOwner = this

        binding.viewmodel = viewModel
        viewModel.bookResult.observe(viewLifecycleOwner, Observer {
            resultAdapter.submitList(it)
        })


        mRecyclerView = binding.recyclerviewResult
        linerLayoutManager = LinearLayoutManager(requireContext())
        linerLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = linerLayoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL))

        resultAdapter = ResultAdapter(ResultAdapter.OnClickListener(){
            viewModel.addDetailBook(it)
            this.findNavController().navigate(ResultListFragmentDirections.actionResultListFragmentToDetailFragment())
        })
        mRecyclerView.adapter = resultAdapter

        return binding.root



    }
}