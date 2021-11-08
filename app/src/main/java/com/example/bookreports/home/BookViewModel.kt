package com.example.bookreports.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.data.book.detailbook.Book
import com.example.bookreports.data.book.detailbook.Comment
import com.example.bookreports.data.book.detailbook.selectedBook
import com.example.bookreports.data.book.writecomment.commentFail
import com.example.bookreports.data.book.writecomment.commentSuccess
import com.example.bookreports.data.book.writecomment.editcomment.editCommentFail
import com.example.bookreports.data.book.writecomment.editcomment.editCommentSuccessful
import com.example.bookreports.data.register.register_error.register_error
import com.example.bookreports.network.BookApi
import com.example.bookreports.utils.SingleLiveEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class BookViewModel:ViewModel() {


    private val _recommendBooks =  MutableLiveData<List<BookItem>>()
    val recommendBooks: LiveData<List<BookItem>>
    get() = _recommendBooks



    private val _commentSuccess = SingleLiveEvent<com.example.bookreports.data.book.writecomment.Comment>()
    val commentSuccess: SingleLiveEvent<com.example.bookreports.data.book.writecomment.Comment>
    get() = _commentSuccess

    private val _commentFail = SingleLiveEvent<commentFail>()
    val commentFail: SingleLiveEvent<commentFail>
    get() = _commentFail

    private val _editCommentSuccess = SingleLiveEvent<editCommentSuccessful>()
    val editCommentSuccess: SingleLiveEvent<editCommentSuccessful>
    get() = _editCommentSuccess


    private val _editCommentFail = SingleLiveEvent<editCommentFail>()
    val editeCommentFail: SingleLiveEvent<editCommentFail>
    get() = _editCommentFail

    private val _deleteCommentState = SingleLiveEvent<String>()
    val deleteCommentState: SingleLiveEvent<String>
    get() = _deleteCommentState


    private val _selectedBook = MutableLiveData<selectedBook>()
    val selectedDetail: LiveData<selectedBook>
    get() = _selectedBook

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
    get() = _comments


    private val _bookResult = SingleLiveEvent<List<BookItem>>()
    val bookResult: SingleLiveEvent<List<BookItem>>
    get() = _bookResult






    private val _searchBookName = MutableLiveData<String>()
    val searchBookName: LiveData<String>
    get() = _searchBookName

    init {
        Timber.d("BookViewModel start")
    }

    fun getBooksFromApi() {

//        var fakeList: List<BookItem>
//        val job = viewModelScope.launch{
//            Timber.d("測試 current Thread ${Thread.currentThread()}")
//            Timber.d("測試 currentCoroutineContext  ${currentCoroutineContext()}")
//        }
//        val


        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("目前thread ${currentCoroutineContext()}")
            val list = listOf<BookItem>()
            val result = BookApi.retrofitService.apiGetBook()
            try {
                val resultList = result.await()
                if(resultList.isSuccessful){
                    _recommendBooks.postValue(resultList.body())
                }else{
                    _recommendBooks.postValue(list)
                }

            } catch (e: Exception) {
                Timber.d("getBooksFail e: $e")
                _recommendBooks.postValue(list)
            }
        }
    }

    fun getSelectedBookDetailFromApi(bookId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = BookApi.retrofitService.apiGetSelectedBookDetail(bookId)
            try {
                val resultList = result.await()
                _selectedBook.postValue(resultList.body())
                _comments.postValue(resultList.body()?.book?.comments)
                Timber.d("selectedBook: ${resultList.body()}")
                Timber.d("comment: ${resultList.body()?.book?.comments}")
            }catch (e:Exception){
                Timber.d("getDetailFail e:$e")
            }

        }
    }

     fun searchByBookNameFromApi(bookName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("Testing viewModelScope  $bookName")
            val result = BookApi.retrofitService.apiSearchBook(bookName)
            Timber.d("Testing result $result")
            Timber.d("Testing searchBookName : $bookName")

            try {
                val getResult = result.await()
                Timber.d("Testing try ")
                Timber.d("Testing getResult ${getResult.body()}")
                if (getResult.isSuccessful) {
                    _searchBookName.postValue(bookName)
                    Timber.d("Testing getResult.isNotEmpty $bookName is success")
                    // TODO: 2021/7/2 把這邊做filter 看一下生命週期 
                    _bookResult.postValue(getResult.body())
                    Timber.d("Testing _bookResult.postValue ")
                } else{
                    Timber.d("Testing getResult fail ${getResult.errorBody()}")
                    val list = listOf<BookItem>()
                    _bookResult.postValue(list)
                }
            } catch (e: Exception) {
                if (e.message == "Expected BEGIN_ARRAY but was STRING at line 1 column 1 path \$"){
                    Timber.d("沒結果")
                    _bookResult.postValue(emptyList())
                }else{
                    Timber.d("其他錯誤")
                }

            }
        }
    }

    fun writeCommentToApi(starCount: Float, comment: String?,token: String){
        viewModelScope.launch(Dispatchers.IO) {
            val id = selectedDetail.value?.book?.id
            val gson = Gson()
            val type = object : TypeToken<commentFail>() {}.type
            Timber.d("startCount:$starCount comment:$comment token:$token id:$id" )
//          val result = id?.let { BookApi.retrofitService.apiWriteComment(it,starCount,comment,token) }
            if (id != null){
                val result = BookApi.retrofitService.apiWriteComment(id,starCount,comment,token)
                try {
                    val resultList = result.await()
                    if (resultList.isSuccessful){
                        _commentSuccess.postValue(resultList.body())
                        Timber.d("resultList successful")
                    }else{
                        val errorMessage = gson.fromJson<commentFail>(resultList.errorBody().toString(),type)
                        _commentFail.postValue(errorMessage)
                        Timber.d("errorMessage: ${errorMessage.message}")
                    }
                }catch (e:Exception){
                    Timber.d("writeComment fail e: $e")
                }
            }else{
                Timber.d("沒有點選的 id")
            }
        }
    }

    fun resetResultBook(){
        _bookResult.postValue(null)
    }


    fun editComment(commentId: Int,token: String,rate: Float,comment: String?){

        val gson = Gson()
        val type = object : TypeToken<editCommentFail>() {}.type

        viewModelScope.launch(Dispatchers.IO) {
            val result = BookApi.retrofitService.apiEditComment(commentId,token,rate,comment)
            try {
                val resultList = result.await()
                if (resultList.isSuccessful){
                    _editCommentSuccess.postValue(resultList.body())
                    Timber.d("編輯留言成功")
                }else{
                    val errorMessage = gson.fromJson<editCommentFail>(resultList.errorBody().toString(),type)
                    _editCommentFail.postValue(errorMessage)
                    Timber.d("編輯留言失敗")
                }
            }catch (e:Exception){
                Timber.d("editComment fail e:$e")
            }
        }
    }

    fun deleteComment(commentId: Int,token: String){

        viewModelScope.launch(Dispatchers.IO) {
            val result = BookApi.retrofitService.apiDeleteComment(commentId,token)
            try {
                val resultList = result.await()
                if(resultList.isSuccessful){
                    Timber.d("deleteComment successful")
                    _deleteCommentState.postValue("successful")
                }else{
                    Timber.d("deleteComment fail")
                    _deleteCommentState.postValue("fail")
                }
            }catch (e:Exception){
                Timber.d("deleteComment fail e: $e")
                _deleteCommentState.postValue("fail")
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        Timber.d("BookViewModel  End")
    }



}