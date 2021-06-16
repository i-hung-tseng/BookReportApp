package com.example.bookreports.network


import com.example.bookreports.BuildConfig
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.data.book.detailbook.selectedDetail
import com.example.bookreports.data.login.userLogin
import com.example.bookreports.data.logout.logOut
import com.example.bookreports.data.profile_upload.ProfileImage
import com.example.bookreports.data.register.registerUser
import com.example.bookreports.data.register.register_success.userFromApi
import com.example.bookreports.data.userprofile.UserProfile
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Headers
import timber.log.Timber
import java.util.concurrent.TimeUnit


interface BookApiService {


    //suspend 跟 Call 只能有其中一個
    //註冊應該要有成功跟失敗
    @Headers("Accept: application/json")
    @POST("register")
    fun apiRegister(@Body registerUser: registerUser): Deferred<Response<userFromApi>>

    @Headers("Accept: application/json")
    @POST("login")
    fun apiLogin(@Body userLogin: userLogin): Deferred<Response<userFromApi>>


    @POST("logout")
    fun apiLogOut(@Header("authorization") token: String?): Deferred<Response<logOut>>

    @GET("books")
    fun apiGetBook(): Deferred<List<BookItem>>



    @Multipart
//    @Headers("Content-Type: multipart/form-data")
    @POST("http://52.196.162.105/api/profile/{user_id}/photo")
    fun apiUploadProfile(
        @Path("user_id") userId: String,
        @Query("_method") string: String,
        @Part image: MultipartBody.Part,
        @Header("authorization") token: String?
    ): Deferred<Response<ProfileImage>>


    @FormUrlEncoded
    @POST("http://52.196.162.105/api/books/{bookID}/comment")
    fun apiWriteComment(
        @Path("bookID") bookId: Int,
        @Field("rate") rate: Float,
        @Field("comment") comment: String?,
        @Header("authorization") token: String?
    ): Deferred<Response<ResponseBody>>


    @GET("http://52.196.162.105/api/books/search/{bookname}")
    fun apiSearchBook(@Path("bookname") bookName: String): Deferred<List<BookItem>>

    @GET("http://52.196.162.105/api/profile/{user_id}")
    fun apiGetProfileInfo(
        @Path("user_id") userId: String,
        @Header("authorization") token: String?
    ): Deferred<UserProfile>

    @GET("http://52.196.162.105/api/books/category/{category_id}")
    fun apiGetCategoryBook(@Path("category_id") id: String): Deferred<List<BookItem>>

    @GET("http://52.196.162.105/api/books/{book_id}")
    fun apiGetSelectedBookDetail(@Path("book_id") bookId: Int?): Deferred<Response<selectedDetail>>

}


object BookApi {

    private val BASE_URL = "http://52.196.162.105/api/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if(BuildConfig.DEBUG){HttpLoggingInterceptor.Level.BODY}else
        {HttpLoggingInterceptor.Level.NONE})


    val client = OkHttpClient.Builder()
        //新增攔截器統一追加參數
        .addInterceptor(object : Interceptor {

            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val newRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .header("Content-Type","multipart/form-data")
                    .build()


                return chain.proceed(newRequest)
            }
        })
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(client)
        .build()


    val retrofitService = retrofit.create(BookApiService::class.java)


}
