package com.example.bookreports.detail


import android.app.AlertDialog
import android.graphics.Color
import android.media.Rating
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
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentDetailBinding
import com.example.bookreports.home.BookViewModel
import com.example.bookreports.registerinfo.AccountViewModel
import com.example.bookreports.utils.userInfo
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.math.BigDecimal
import java.text.DecimalFormat


class DetailFragment : Fragment(),RatingBar.OnRatingBarChangeListener {

    lateinit var binding: FragmentDetailBinding
    lateinit var commentAdapter: CommentAdapter
    private val vmBook: BookViewModel by sharedViewModel()
    private val vmAccount: AccountViewModel by sharedViewModel()
    private var alreadyCommentIndex :Int? = null
    lateinit var list: MutableList<Int>
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater)

        vmBook.selectedDetail.observe(viewLifecycleOwner, Observer {
            setAverageRating()
            vmBook.selectedDetail.value?.avg_rate?.toFloat().let {
                if (it != null) {
                    binding.ratingBar.rating = it
                }
            }
        })






        vmBook.deleteCommentState.observe(viewLifecycleOwner, Observer {
            Timber.d("進到 observe deleteCommentState it:$it")
            if (it == "successful"){
                Toast.makeText(requireActivity(),"刪除成功",Toast.LENGTH_SHORT).show()
                val token = "Bearer " + vmAccount.accountInfo.value?.token
                vmAccount.getProfileInfo(vmAccount.accountInfo.value?.user?.id.toString(),token)
                vmBook.selectedDetail.value?.book?.id?.let { it1 -> vmBook.getSelectedBookDetailFromApi(it1) }
            }else{
                Toast.makeText(requireActivity(),"刪除沒成功",Toast.LENGTH_SHORT).show()
            }
        })

        vmBook.commentFail.observe(viewLifecycleOwner, Observer {
           if( it.errors.rate[0] ==  "The rate field is required."){
               Toast.makeText(requireActivity(),"星數為必須填寫",Toast.LENGTH_SHORT).show()
           }else{
               Toast.makeText(requireActivity(),"星數必須為1-5",Toast.LENGTH_SHORT).show()
           }
        })

        vmBook.commentSuccess.observe(viewLifecycleOwner, Observer {
            Timber.d("評論成功")
            Toast.makeText(requireActivity(),"新增評論已完成，恭喜您獲得5 point",Toast.LENGTH_SHORT).show()
            val token = "Bearer " + vmAccount.accountInfo.value?.token
            vmAccount.getProfileInfo(vmAccount.accountInfo.value?.user?.id.toString(),token)
            vmBook.selectedDetail.value?.book?.id?.let { it1 -> vmBook.getSelectedBookDetailFromApi(it1) }
        })

        vmBook.editCommentSuccess.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireActivity(),"編輯留言已成功",Toast.LENGTH_SHORT).show()
            val token = "Bearer " + vmAccount.accountInfo.value?.token
            vmAccount.getProfileInfo(vmAccount.accountInfo.value?.user?.id.toString(),token)
            vmBook.selectedDetail.value?.book?.id?.let { it1 -> vmBook.getSelectedBookDetailFromApi(it1) }
        })

        binding.btnAddComment.setOnClickListener {
            if (checkCommentOrNot() == true){
                Timber.d("有留言")
                showComment()
                val userId = vmAccount.accountInfo.value?.user?.id.toString()
                val token = "Bearer " + vmAccount.accountInfo.value?.token
                vmAccount.getProfileInfo(userId,token)
            }else{
                showEditDialog()
                Timber.d("沒留言過")
            }
        }


        initBind()
        initAdapter()


//        vmBook.comments.observe(viewLifecycleOwner, Observer {
//            if (it != null){
//                val text = "${it.size}" + "筆評論"
//                binding.tvTotalComment.text = text
//            }else{
//                val text = "0筆評論"
//                binding.tvTotalComment.text = text
//            }
//        })

        val nav = this.findNavController()




        return binding.root
    }


    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {

        Timber.d("onChange $rating")
    }

    private fun initAdapter() {
        val myRecy = binding.recyComment
        val linearManager = LinearLayoutManager(requireContext())
        linearManager.orientation = LinearLayoutManager.VERTICAL
        myRecy.layoutManager = linearManager
        commentAdapter = CommentAdapter()
        myRecy.adapter = commentAdapter
    }

    private fun initBind() {
        binding.lifecycleOwner = this
        binding.tvDescrip
        //書籍介紹可滑動
        binding.tvDescrip.movementMethod = ScrollingMovementMethod()
        binding.bookViewModel = vmBook


    }
//
//    private fun setTotalCount(){
//        if(vmBook.selectedDetail.value?.book?.comments?.size != null){
//        val text = "${vmBook.selectedDetail.value?.book?.comments?.size}" + "篇評論"
//        Timber.d("text: $text")
//        binding.tvTotalComment.text = text
//        }else{
//            val text = "0篇評論"
//            binding.tvTotalComment.text = text
//        }
//    }


    private fun setAverageRating() {
        val str: String = String.format("%.2f", (vmBook.selectedDetail.value?.avg_rate?.toFloat()))
        binding.tvAverageRating.text = str
    }


    private fun showEditDialog() {
            val builder = AlertDialog.Builder(requireActivity())
            val dialogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_writecomment, null)
            val ratingBar = dialogLayout.findViewById<RatingBar>(R.id.rating_dialog)
            var start: Float? = null
            ratingBar.onRatingBarChangeListener = this
            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> start = rating }
            with(builder) {
                setTitle("${vmBook.selectedDetail.value?.book?.bookname}")
                setPositiveButton("張貼") { dialog, which ->
                    val editText = dialogLayout.findViewById<EditText>(R.id.edit_dialog)
                    Timber.d("editText :　$editText")
                    val editWord: String? = editText.text.toString()
                    if (start != null && start != 0.0F) {
                        start?.let { vmBook.writeCommentToApi(it, editWord, "Bearer " + vmAccount.accountInfo.value?.token) }
                        val id = vmAccount.accountInfo.value?.user?.id.toString()
                        val token = "Bearer " + vmAccount.accountInfo.value?.token
                        Timber.d("Testing token : $token")
                        vmAccount.getProfileInfo(id, token)

                    } else {
                        Toast.makeText(requireActivity(), "星級數為必填寫選項", Toast.LENGTH_SHORT).show()
                    }
                }
                setNegativeButton("取消") { dialog, which ->
                    Timber.d("點取消")
                }
                setView(dialogLayout)
                show()
            }
    }

    private fun showComment() {
        val builder = AlertDialog.Builder(requireActivity())
        val diaLogLayout = requireActivity().layoutInflater.inflate(R.layout.dialog_seecomment, null)
        val ratingBar = diaLogLayout.findViewById<RatingBar>(R.id.ratingBar3)
//        val commentId = alreadyCommentIndex?.let { vmAccount.profileInfo.value?.user?.comments?.get(it)?.id }
        val commentId = alreadyCommentIndex?.let { vmBook.selectedDetail.value?.book?.comments?.get(it)?.id }
        val token = "Bearer " + vmAccount.accountInfo.value?.token
        var start: Float? = null
        ratingBar.onRatingBarChangeListener = this
//        val rating = alreadyCommentIndex?.let { vmAccount.profileInfo.value?.user?.comments?.get(it)?.rate }
        val rating = alreadyCommentIndex?.let { vmBook.selectedDetail.value?.book?.comments?.get(it)?.rate }
        if(rating != null){
            ratingBar.rating = rating
        }
        val editText = diaLogLayout.findViewById<EditText>(R.id.edit_forComment)
        Timber.d("editText: $editText")
//        val comment = alreadyCommentIndex?.let { vmAccount.profileInfo.value?.user?.comments?.get(it)?.comment }
        val comment = alreadyCommentIndex?.let { vmBook.selectedDetail.value?.book?.comments?.get(it)?.comment }
        Timber.d("comment $comment")
        editText.setText(comment)
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> start = rating }
        with(builder) {
            setTitle("${vmBook.selectedDetail.value?.book?.bookname}")
            setPositiveButton("編輯留言") { dialog, which ->
                Timber.d("enter 編輯留言")
                Timber.d("enter 編輯留言 start $start")
                start = ratingBar.rating
//                if (start != null && start != 0.0F && commentId != null ){
//                    Timber.d("編輯 comment")
//                    val editWord = editText.text.toString()
//                    start?.let { vmBook.editComment(commentId,token,it,editWord) }
//                }
                if (start != 0.0f){
                    Timber.d("編輯comment start != 0.0f ")
                    val editWord = editText.text.toString()
                    start?.let {
                        if (commentId != null) {
                            vmBook.editComment(commentId,token,it,editWord)
                        }
                    }
                }else{
                    Toast.makeText(requireActivity(),"星數不得為0",Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("刪除留言"){ dialog, which ->
                commentId?.let { vmBook.deleteComment(it,token) }
            }
            setNeutralButton("關閉"){dialog, which ->
                Timber.d("關閉留言")
            }
            setView(diaLogLayout)
            show()
        }
    }


    private fun checkCommentOrNot(): Boolean {

        val selectedBookId = vmBook.selectedDetail.value?.book?.id
        var comment: Boolean = false
        val commentSize = vmAccount.profileInfo.value?.countOfComments
        Timber.d("enter CommentSize selected: $selectedBookId   commentsize: $commentSize")
        if (commentSize != null) {
            Timber.d("enter commentSize != null $commentSize")
            vmAccount.profileInfo.value?.user?.comments?.filter { selectedBookId == it.bookID }
            Timber.d("Testing ${vmAccount.profileInfo.value?.user?.comments?.filter { selectedBookId == it.bookID }}")
            for (i in 0 until commentSize) {
                Timber.d("enter forLoop i:$i")
                Timber.d("enter forLoop commentBookId:${vmAccount.profileInfo.value?.user?.comments?.get(i)?.bookID}")
                if (selectedBookId == vmAccount.profileInfo.value?.user?.comments?.get(i)?.bookID) {
                    Timber.d("enter")
                      checkCommentIndex()
                    Timber.d("您回來啦 checkCommentIndex(): ${checkCommentIndex()} ")
                    Timber.d("您回來啦 i: $i")
//                    alreadyCommentIndex = i
                    Timber.d("alreadyCommentIndex: $alreadyCommentIndex")
                    return true
                }else{
                    comment = false
                    Timber.d("沒有評論過")
                }
            }
        }
        return comment
    }

    private fun checkCommentIndex() {

        val userId = vmAccount.accountInfo?.value?.user?.id
        val selectedBookComments = vmBook.selectedDetail?.value?.book?.comments
        if (selectedBookComments?.size != null){
            Timber.d("您回來啦 enterSelectedBookComments != null")
            for (i in selectedBookComments.indices){
                if (selectedBookComments[i].userID == userId){
                    alreadyCommentIndex = i
                    Timber.d(" 您回來啦 selectedBookComment[i].userId I: $i")
                }else{
                    Timber.d("您回來啦 else")
                }
            }

        }
    }

}

