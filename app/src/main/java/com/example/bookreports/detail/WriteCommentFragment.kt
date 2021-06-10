package com.example.bookreports.detail

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentWriteCommentBinding
import com.example.bookreports.utils.MainViewModel

class WriteCommentFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()
    lateinit var pb_writeComment: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentWriteCommentBinding.inflate(inflater)
        val ratingBar = binding.ratingBar2
        pb_writeComment = binding.pbWriteComment
        val btnSend = binding.btnSendComment
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.commentResponse.observe(viewLifecycleOwner, Observer {
            if (it == "successful"){
                Toast.makeText(requireActivity(),"感謝您新增心得",Toast.LENGTH_SHORT).show()
                pb_writeComment.visibility = View.GONE
            }
        })

        viewModel.alreadyComment.observe(viewLifecycleOwner, Observer {
            if(it != null){
                Toast.makeText(requireContext(),"已經有評論過囉!!",Toast.LENGTH_SHORT).show()
            }
        })




        if(viewModel.rateNum.value != null ){
            ratingBar.rating = viewModel.startNum.value!!
        }else{
            ratingBar.rating = 0.0f
        }

        if (viewModel.alreadyComment.value?.comment != null) {
            ratingBar.rating = viewModel.alreadyComment.value?.rate!!
            binding.edComment.visibility = View.GONE
            binding.tvCommentWord.apply {
                visibility = View.VISIBLE
                setText(viewModel.alreadyComment.value?.comment)
            }

        } else {
            ratingBar.rating = 0.0f
        }







        btnSend.setOnClickListener {
            val getComment = binding.edComment.text.toString()
            val ratingCount = ratingBar.rating
            if (ratingCount > 0.0f && getComment.isNotEmpty()){
                viewModel.writeComment(ratingCount,getComment)
            }else if (ratingCount != 0.0f){
                viewModel.writeComment(ratingCount,null)
            }else{
                Toast.makeText(requireActivity(),"評分不得為0",Toast.LENGTH_SHORT).show()
            }
        }





        return binding.root
    }

    override fun onDestroy() {
        viewModel.reSetAlreadyRate()
        viewModel.addGoBack()
        super.onDestroy()
    }
}