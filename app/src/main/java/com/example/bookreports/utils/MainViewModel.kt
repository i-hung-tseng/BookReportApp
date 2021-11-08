package com.example.bookreports.utils



import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.data.register.register_error.register_error
import com.example.bookreports.data.register.register_success.userFromApi
import com.example.bookreports.data.userprofile.Comment
import com.example.bookreports.data.userprofile.UserProfile
import com.example.bookreports.network.BookApi
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.IOException
import java.lang.Exception






class MainViewModel():ViewModel() {




    private val _firstComment = MutableLiveData<String>()
    val firstComment : LiveData<String>
    get() = _firstComment



//    private val _loginFail = MutableLiveData<String>()
//    val loginFail: LiveData<String>
//    get() = _loginFail


    private val _searchName = MutableLiveData<String>()
    val searchName: LiveData<String>
    get() = _searchName




    private val _fetchApiError = MutableLiveData<String>()
    val fetchApiError: LiveData<String>
    get() = _fetchApiError


    private val _canGoBack = MutableLiveData<String>()
    val canGoBack: LiveData<String>
    get() = _canGoBack



    private val _notifacationBookCount = SingleLiveEvent<List<BookItem>>()
    val notificationBookCount: SingleLiveEvent<List<BookItem>>
    get() = _notifacationBookCount




    private val _firstTimeLogin = MutableLiveData<Boolean>()
    val firstTimeLogin: LiveData<Boolean>
    get() = _firstTimeLogin


    private val _rateNum = SingleLiveEvent<Float>()
    val rateNum: SingleLiveEvent<Float>
    get() = _rateNum

    private val _alreadyComment = SingleLiveEvent<Comment>()
    val alreadyComment:SingleLiveEvent<Comment>
        get() = _alreadyComment


//    private val _selectedBookDetail = MutableLiveData<selectedDetail>()
//    val selectedBookDetail: LiveData<selectedDetail>
//    get() = _selectedBookDetail


    private val _categoryBookList = MutableLiveData<List<BookItem>>()
    val categoryBookList: LiveData<List<BookItem>>
    get() = _categoryBookList



    var list: MutableList<String> = mutableListOf("","","","","")

    private val _startNum = MutableLiveData<Float>()
    val startNum:LiveData<Float>
    get() = _startNum

    private val _commentResponse = MutableLiveData<String>()
    val commentResponse : LiveData<String>
    get() = _commentResponse


    private val _bookResult = MutableLiveData<List<BookItem>>()
    val bookResult: LiveData<List<BookItem>>
    get() = _bookResult





    private val _selectedBook = MutableLiveData<BookItem>()
    val selectedBook: LiveData<BookItem>
    get() = _selectedBook




    private val _logOutState = MutableLiveData<String>()
    val logOutState : LiveData<String>
    get() = _logOutState




    //登入後帳號
    private val _registerAccount = MutableLiveData<userFromApi>()
    val registerAccount: LiveData<userFromApi>
        get() = _registerAccount


    private val _accountProfile = MutableLiveData<UserProfile>()
    val accountProfile: LiveData<UserProfile>
    get() = _accountProfile






    //註冊失敗訊息
    private val _registerError = MutableLiveData<register_error>()
    val registerError: LiveData<register_error>
    get() = _registerError







    private val _newMemberOrAlreadyMember = MutableLiveData<String>()
    val newMemberOrAlreadyMember: LiveData<String>
        get() = _newMemberOrAlreadyMember



    private val _categoryPhoto = MutableLiveData<List<String>>()
    val categoryPhoto: LiveData<List<String>>
        get() = _categoryPhoto


    private val _recommendBooks = MutableLiveData<List<BookItem>>()
    val recommendBooks: LiveData<List<BookItem>>
        get() = _recommendBooks




    //判斷是否有註冊
    fun defineNewMemberOrAlreadyMember(str: String?) {
        _newMemberOrAlreadyMember.postValue(str)
    }

    private val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope(viewmodelJob + Dispatchers.Main)



init {
//    getBooksFromApi()
    _bookResult.postValue(listOf())
//    _firstTimeLogin.postValue(true)
}


//    註冊功能
//    fun registerUser() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val userInfo = registerUser(
//                    list[0],
//                    list[1],
//                    list[2],
//                    list[3],
//                    list[4]
//            )
//            try {
//                val gson = Gson()
//                val type = object : TypeToken<register_error>() {}.type
//                val result = BookApi.retrofitService.apiRegister(userInfo).await()
//                if (result.isSuccessful) {
//                    Timber.d("是成功的")
//                    val success = result.body()
//                    if (success != null) {
//                        Timber.d("successful")
//                        _registerAccount.postValue(success)
//
//                    } else {
//                        Timber.d(" su & erall null")
//                    }
//                } else {
////                        val error = result.errorBody()?.string()
//                    val error: register_error = gson.fromJson(result.errorBody()?.string(), type)
//                    _registerError.postValue(error)
//                    Timber.d(" error $error")
//                }
//            } catch (e: Exception) {
//            }
//
//        }
//    }
//
//    fun logOut() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val token = "Bearer " + _registerAccount.value?.token
//            try {
//                val gson = Gson()
//                val type = object : TypeToken<logFail>() {}.type
//                val result = BookApi.retrofitService.apiLogOut(token).await()
//                withContext(Dispatchers.Main) {
//                    if (result.isSuccessful) {
//                        _registerAccount.postValue(null)
//                        _logOutState.postValue(result.body()?.message)
//                    } else {
//                        val error: logFail = gson.fromJson(result.errorBody()?.string(), type)
//                        _logOutState.postValue(error.message)
//                    }
//                }
//            } catch (e: Exception) {
//                Timber.d("logout fail e:$e")
//            }
//        }
//    }


//
//    fun getSelectedBookDetailFromApi(bookId: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            val result = BookApi.retrofitService.apiGetSelectedBookDetail(bookId)
//            try {
//                val resultList = result.await()
//                _selectedBookDetail.postValue(resultList.body())
//            }catch (e:Exception){
//            }
//
//
//        }
//
//
//
//    }



//    fun login(email: String, passWord: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val loging = userLogin(email, passWord)
//            val gson = Gson()
//            val type = object : TypeToken<loginFail>() {}.type
//            try {
//                Timber.d("login ${currentCoroutineContext()}")
//                val result = BookApi.retrofitService.apiLogin(loging).await()
//                if (result.isSuccessful) {
//                    _registerAccount.postValue(result.body())
//                } else{
//                 val error:loginFail = gson.fromJson(result.errorBody()?.string(),type)
//                 _loginFail.postValue(error.message)
//
//                }
//            } catch (e: Exception) {
//                Timber.d("loginFail e: $e")
//            }
//        }
//    }
//
//
//    private fun searchByBookNameByApi(bookName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = BookApi.retrofitService.apiSearchBook(bookName)
//            try {
//                val getResult = result.await()
//                if (getResult.size > 0) {
//                    _bookResult.postValue(getResult)
//                } else {
//                    _bookResult.postValue(listOf())
//                }
//            } catch (e: Exception) {
//            }
//        }
//    }


//    private fun getBooksFromApi() {
//        viewModelScope.launch(Dispatchers.IO) {
//            Timber.d("目前thread ${currentCoroutineContext()}")
//            val result = BookApi.retrofitService.apiGetBook()
//            try {
//                val resultList = result.await()
//                _recommendBooks.postValue(resultList)
//            } catch (e: Exception) {
//            }
//        }
//    }



//    fun getUserInfoFromApi(){
//        viewModelScope.launch(Dispatchers.IO) {
//            val token = "Bearer " +_registerAccount.value?.token
//            val result = BookApi.retrofitService.apiGetProfileInfo(_registerAccount.value?.user?.id.toString(),token)
//            try {
//              val resultList = result.await()
//              _accountProfile.postValue(resultList)
//            }catch (e:Exception){
//                _fetchApiError.postValue(e.message)
//            }
//        }
//    }



//    fun resetLogOut(){
//        _logOutState.postValue(null)
//    }


    fun addDetailBook(item:BookItem){
        _selectedBook.postValue(item)
    }

    fun deleteDetailBook(){
        _selectedBook.postValue(null)
    }


    fun writeComment(token: String,rate: Float, comment: String?) {
        viewModelScope.launch(Dispatchers.IO) {
//            val token = "Bearer " +_registerAccount.value?.token
            try {
                val result = BookApi.retrofitService.apiWriteComment(selectedBook.value!!.id,rate,comment,token).await()
                if (result.isSuccessful) {
                    _commentResponse.postValue("successful")

                        }else{
                }
            }catch (e:Exception){
            }
        }
    }

    fun setRateNum(num:Float){
        _startNum.postValue(num)
    }

    fun resetRateNum(){
        _startNum.postValue(null)
    }

    fun searchByName(bookName: String){
//        searchByBookNameByApi(bookName)
        _searchName.postValue(bookName)
    }

    fun setAlreadyRate(float: Float){
        _rateNum.postValue(float)
    }

    fun reSetAlreadyRate(){
        _rateNum.postValue(10f)
    }

//    @SuppressLint("LogNotTimber")
//    fun uploadImage(requestFile: MultipartBody.Part) {
//
//        val token = "Bearer " +_registerAccount.value?.token
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = BookApi.retrofitService.apiUploadProfile(accountProfile.value?.user?.id.toString(),"PUT",requestFile,token).await()
//            if(result.isSuccessful){
//                Timber.d("upload成功")
//            }else{
//                try {
//                    Timber.d("upload失敗errorbody ${result.errorBody()?.string()}")
//                }catch (e:IOException){
//                    Timber.d("upload失敗e ${e.message}")
//                }
//            }
//        }
//    }



    fun getCategoryBookListFromApi(category: String,rankMethod: String?){
        viewModelScope.launch(Dispatchers.IO) {
            val result = BookApi.retrofitService.apiGetCategoryBook(category)
            try {
                if(rankMethod == null){
                val resultList = result.await()
                _categoryBookList.postValue(resultList)
                _notifacationBookCount.postValue(resultList)
                }else if (rankMethod == "上架時間"){
                val resultList = result.await()
                val afterresult =resultList.sortedBy { it.publish_date }
                _categoryBookList.postValue(afterresult)
                }else{
                }
            }catch (e:Exception){
            }
        }

    }

    fun addAlreadyComment(comment: Comment?){
        _alreadyComment.postValue(comment)
    }


    fun addGoBack(){
        _canGoBack.postValue("不觀察")
    }

    fun resetGoBack(){
        _canGoBack.postValue("直接觀察")
    }

    fun addFirstComment(){
        _firstComment.postValue("YES")
    }

    fun resetFirstComment(){
        _firstComment.postValue("NOT")
    }


    fun resetFirestLogin(){
        _firstTimeLogin.postValue(false)
    }

    fun resetRegisterAccount(){
        _registerAccount.postValue(null)

    }

    fun resetRegisterError(){
        _registerError.postValue(null)
    }
//
//    fun resetLoginFail(){
//        _loginFail.postValue(null)
//    }

}




