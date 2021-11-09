package com.example.bookreports.registerinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreports.data.book.detailbook.Comment
import com.example.bookreports.data.login.login_fail.loginFail
import com.example.bookreports.data.login.userLogin
import com.example.bookreports.data.logout.logFail
import com.example.bookreports.data.logout.logOut
import com.example.bookreports.data.register.registerUser
import com.example.bookreports.data.register.register_error.register_error
import com.example.bookreports.data.register.register_success.userFromApi
import com.example.bookreports.data.userprofile.Book
import com.example.bookreports.data.userprofile.UserProfile
import com.example.bookreports.network.BookApi
import com.example.bookreports.utils.SingleLiveEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

class AccountViewModel: ViewModel() {




    private val _newOrNot = SingleLiveEvent<Boolean>()
    val newOrNot: SingleLiveEvent<Boolean>
    get() = _newOrNot

    private val _profileFail = SingleLiveEvent<String>()
    val profileFail: SingleLiveEvent<String>
    get() = _profileFail


    private val _accountInfo =  MutableLiveData<userFromApi>()
    val accountInfo: LiveData<userFromApi>
    get() = _accountInfo

    private val _profileInfo = MutableLiveData<UserProfile>()
    val profileInfo: LiveData<UserProfile>
    get() = _profileInfo


    private val _registerFail = SingleLiveEvent<register_error>()
    val registerFail: SingleLiveEvent<register_error>
    get() = _registerFail


    private val _logOutState = SingleLiveEvent<logOut>()
    val logOutState: SingleLiveEvent<logOut>
    get() = _logOutState


    private val _loginFail = SingleLiveEvent<loginFail>()
    val loginFail: SingleLiveEvent<loginFail>
    get() = _loginFail

    var list: MutableList<String> = mutableListOf("","","","","")


     fun registerAccount(){

        val gson = Gson()
        val type = object : TypeToken<register_error>() {}.type
        viewModelScope.launch(Dispatchers.IO){
            val registerInfo = registerUser(list[0],list[1],list[2],list[3],list[4])
            val getResult = BookApi.retrofitService.apiRegister(registerInfo)
            try {
                val result = getResult.await()
                if (result.isSuccessful){
                    _accountInfo.postValue(result.body())

                }else{
                    val errorMessage: register_error = gson.fromJson(result.errorBody()?.string(),type)
                    _registerFail.postValue(errorMessage)
                }
            }catch (e:Exception){
                Timber.d("註冊錯誤 e:$e")
            }
        }
    }


    fun login(email: String, passWord: String){
        val gson = Gson()
        val type = object : TypeToken<loginFail>() {}.type
        val loginInfo = userLogin(email,passWord)
        viewModelScope.launch(Dispatchers.IO) {
            val getResult = BookApi.retrofitService.apiLogin(loginInfo)
            try {
                val result = getResult.await()
                if (result.isSuccessful){
                 _accountInfo.postValue(result.body())
                }else{
                 val errorMessage: loginFail = gson.fromJson(result.errorBody()?.string(),type)
                 _loginFail.postValue(errorMessage)
                }
            }catch (e:Exception){
                Timber.d("登入失敗 e:$e")
            }

        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val gson = Gson()
                val type = object : TypeToken<logFail>() {}.type
                val result = BookApi.retrofitService.apiLogOut("Bearer " + _accountInfo.value?.token).await()

                    if (result.isSuccessful) {
                        _accountInfo.postValue(null)
                        _logOutState.postValue(result.body())
                    } else {
                        val error: logOut = gson.fromJson(result.errorBody()?.string(), type)
                        _logOutState.postValue(error)
                }
            } catch (e: Exception) {
                Timber.d("logout fail e:$e")
            }
        }
    }

    fun getProfileInfo(userId: String,token: String){
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("enterGetProfileInfo userId:$userId, token:$token")
            val result = BookApi.retrofitService.apiGetProfileInfo(userId,token)
            try {
                val resultList = result.await()
                if (resultList.isSuccessful){
                _profileInfo.postValue(resultList.body())
                Timber.d("getProfileInfo successful")
                }else{
                    _profileFail.postValue("error")
                }
            }catch (e:Exception){
                _profileFail.postValue("error")
                Timber.d("getProfileInfo fail: e $e")
            }
        }
    }

    fun uploadImage(requestFile: MultipartBody.Part) {

        val token = "Bearer " + accountInfo.value?.token
        viewModelScope.launch(Dispatchers.IO) {
            val result = BookApi.retrofitService.apiUploadProfile(accountInfo.value?.user?.id.toString(),"PUT",requestFile,token).await()
            if(result.isSuccessful){
                Timber.d("upload成功")
                getProfileInfo(accountInfo.value?.user?.id.toString(),accountInfo.value!!.token)
            }else{
                try {
                    Timber.d("upload失敗errorbody ${result.errorBody()?.string()}")
                }catch (e: IOException){
                    Timber.d("upload失敗e ${e.message}")
                }
            }
        }
    }

    fun setNewOrNot(boolean: Boolean){
        _newOrNot.postValue(boolean)
    }

    fun resetAccountInfo(){
        _accountInfo.postValue(null)
    }
}


