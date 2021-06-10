package com.example.bookreports.detail


import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentDetailBinding
import com.example.bookreports.utils.MainViewModel
import timber.log.Timber


class DetailFragment : Fragment(), RatingBar.OnRatingBarChangeListener {

    private val viewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentDetailBinding
    lateinit var commentAdapter: CommentAdapter

    lateinit var list: MutableList<Int>
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        var ratingBar = binding.ratingBar
        ratingBar.onRatingBarChangeListener = this

        ratingBar.setOnClickListener {
            Timber.d("ratingBar is clicked")
        }


        //書籍介紹可滑動
        binding.tvDescrip.movementMethod = ScrollingMovementMethod()






        viewModel.rateNum.observe(viewLifecycleOwner, Observer {
            if (viewModel.rateNum.value != 10f && viewModel.firstComment.value == "YES") {
                Timber.d("enter Observe viewmodel.rate: ${viewModel.rateNum.value } first ${viewModel.firstComment.value}")
                if (this.findNavController().currentDestination?.id == R.id.detailFragment) {
                    this.findNavController()
                            .navigate(DetailFragmentDirections.actionDetailFragmentToWriteCommentFragment())
                    viewModel.resetFirstComment()
                } else {

                }
            } else {

            }
        })

        viewModel.alreadyComment.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Timber.d("目前startnum: ${it.rate}")
                ratingBar.rating = it.rate!!
            }
        })

        checkAlreadyComment()
        initAdapter()

        viewModel.selectedBookDetail.observe(viewLifecycleOwner, Observer {

            commentAdapter.submitList(it.comments)
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deleteDetailBook()
    }


    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        viewModel.setRateNum(rating)
        viewModel.setAlreadyRate(rating)
        viewModel.addFirstComment()
        Timber.d("onChange")
    }


    private fun checkAlreadyComment() {

        list = mutableListOf()

        val bookId = viewModel.selectedBook.value?.id
        val commentsSize = viewModel.accountProfile.value?.countOfComments
        if (commentsSize != null) {
            if (commentsSize > 0) {
                for (i in 0 until commentsSize) {
                    list = mutableListOf<Int>()
                    viewModel.accountProfile?.value?.user?.comments?.get(i)?.bookID?.let {
                        list.add(
                                it
                        )
                    }
                    if (bookId in list) {
                        val alreadyBook =
                                viewModel.accountProfile.value?.user?.comments?.get(i)?.bookID
                        viewModel.addAlreadyComment(
                                viewModel.accountProfile.value?.user?.comments?.get(
                                        i
                                )
                        )
                    } else {
                    }
                }
            }
        }


    }

    private fun initAdapter() {
        val myRecy = binding.recyComment
        val linearManager = LinearLayoutManager(requireContext())
        linearManager.orientation = LinearLayoutManager.VERTICAL
        myRecy.layoutManager = linearManager
        commentAdapter = CommentAdapter()
        myRecy.adapter = commentAdapter
    }


}

