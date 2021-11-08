//package com.example.bookreports.detail
//
//import android.app.AlertDialog
//import android.app.Dialog
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.EditText
//import android.widget.ProgressBar
//import android.widget.RatingBar
//import android.widget.Toast
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.Observer
//import com.example.bookreports.R
//import com.example.bookreports.databinding.FragmentWriteCommentBinding
//import com.example.bookreports.home.BookViewModel
//import com.example.bookreports.registerinfo.AccountViewModel
//import com.example.bookreports.utils.MainViewModel
//import org.koin.android.ext.android.bind
//import org.koin.androidx.viewmodel.ext.android.sharedViewModel
//import timber.log.Timber
//
//class WriteCommentFragment : Fragment() {
//
//    private val vmAccount: AccountViewModel by sharedViewModel()
//    private val vmBook:BookViewModel by sharedViewModel()
//    private val viewModel: MainViewModel by activityViewModels()
//
//    lateinit var binding: FragmentWriteCommentBinding
//    lateinit var pb_writeComment: ProgressBar
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        binding = FragmentWriteCommentBinding.inflate(inflater)
//        val ratingBar = binding.ratingBar2
//        pb_writeComment = binding.pbWriteComment
//        val btnSend = binding.btnSendComment
//        binding.lifecycleOwner = this
//        binding.viewmodel = viewModel
////
////        showEditDialog()
//
////        viewModel.commentResponse.observe(viewLifecycleOwner, Observer {
////            if (it == "successful"){
////                Toast.makeText(requireActivity(),"感謝您新增心得",Toast.LENGTH_SHORT).show()
////                pb_writeComment.visibility = View.GONE
////            }
////        })
//
////        viewModel.alreadyComment.observe(viewLifecycleOwner, Observer {
////            if(it != null){
////                Toast.makeText(requireContext(),"已經有評論過囉!!",Toast.LENGTH_SHORT).show()
////            }
////        })
//
//
//
////
////        if(viewModel.rateNum.value != null ){
////            ratingBar.rating = viewModel.startNum.value!!
////        }else{
////            ratingBar.rating = 0.0f
////        }
////
////        if (viewModel.alreadyComment.value?.comment != null) {
////            ratingBar.rating = viewModel.alreadyComment.value?.rate!!
////            binding.edComment.visibility = View.GONE
////            binding.tvCommentWord.apply {
////                visibility = View.VISIBLE
////                setText(viewModel.alreadyComment.value?.comment)
////            }
////
////        } else {
////            ratingBar.rating = 0.0f
////        }
//
//
//
//
//
//
////
////        btnSend.setOnClickListener {
////            val getComment = binding.edComment.text.toString()
////            val ratingCount = ratingBar.rating
////            if (ratingCount > 0.0f && getComment.isNotEmpty()){
////                Timber.d("星數&評論都不為null")
////                viewModel.writeComment(vmAccount.token,ratingCount,getComment)
////            }else if (ratingCount != 0.0f){
////                Timber.d("星數不為null")
////                viewModel.writeComment(vmAccount.token,ratingCount,null)
////            }else{
////                Timber.d("評分為null")
////                Toast.makeText(requireActivity(),"評分不得為0",Toast.LENGTH_SHORT).show()
////            }
////        }
////
////        btnSend.setOnClickListener {
////            val getComment = binding.edComment.text.toString()
////            val ratingCount = ratingBar.rating
////            if(ratingCount > 0.0f && getComment.isNotEmpty()){
////
////            }
////        }
//
//
//
//
//
//        return binding.root
//    }
//
//    override fun onDestroy() {
//        viewModel.reSetAlreadyRate()
//        viewModel.addGoBack()
//        super.onDestroy()
//    }
//
//
////    private fun showEditDialog(){
////        binding.btnSendComment.setOnClickListener {
////
////            val builder = AlertDialog.Builder(requireActivity())
////            val inflater = layoutInflater
////            val dialogLayout = inflater.inflate(R.layout.dialog_writecomment,null)
////            val editText = dialogLayout.findViewById<EditText>(R.id.edit_dialog)
////            val ratingBar = dialogLayout.findViewById<RatingBar>(R.id.rating_dialog)
////            with(builder){
////                setTitle("${vmBook.selectedDetail.value?.bookname}")
////                setPositiveButton("張貼"){dialog, which ->
////                Timber.d("點了張貼")
////                }
////                setNegativeButton("取消"){dialog, which ->
////                    Timber.d("點取消")
////                }
////                setView(dialogLayout)
////                show()
////            }
////
////
////        }
////
////    }
//}