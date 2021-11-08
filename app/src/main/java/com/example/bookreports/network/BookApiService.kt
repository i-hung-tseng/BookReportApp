package com.example.bookreports.network


import com.example.bookreports.BuildConfig
import com.example.bookreports.data.book.BookItem
import com.example.bookreports.data.book.detailbook.selectedBook
import com.example.bookreports.data.book.writecomment.Comment
import com.example.bookreports.data.book.writecomment.commentSuccess
import com.example.bookreports.data.book.writecomment.editcomment.editCommentSuccessful
import com.example.bookreports.data.login.userLogin
import com.example.bookreports.data.logout.logOut
import com.example.bookreports.data.profile_upload.ProfileImage
import com.example.bookreports.data.register.registerUser
import com.example.bookreports.data.register.register_success.userFromApi
import com.example.bookreports.data.userprofile.UserProfile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


interface BookApiService {


    @Headers("Accept: application/json")
    @POST("register")
    fun apiRegister(@Body registerUser: registerUser): Deferred<Response<userFromApi>>

    @Headers("Accept: application/json")
    @POST("login")
    fun apiLogin(@Body userLogin: userLogin): Deferred<Response<userFromApi>>


    @POST("logout")
    fun apiLogOut(@Header("authorization") token: String?): Deferred<Response<logOut>>

    @GET("books")
    fun apiGetBook(): Deferred<Response<List<BookItem>>>



    @Multipart
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
    ): Deferred<Response<Comment>>

    @FormUrlEncoded
    @PUT("http://52.196.162.105/api/comments/{comment_id}")
    fun apiEditComment(
            @Path("comment_id") commentId: Int,
            @Header("authorization") token: String?,
            @Field("rate") rate: Float,
            @Field("comment") comment: String?
    ): Deferred<Response<editCommentSuccessful>>

    @DELETE("http://52.196.162.105/api/comments/{comment_id}")
    @Headers("Accept: application/json")
    fun apiDeleteComment(
            @Path("comment_id") commentId: Int,
            @Header("authorization") token: String?
    ): Deferred<Response<String>>



    @GET("http://52.196.162.105/api/books/search/{bookname}")
    fun apiSearchBook(@Path("bookname") bookName: String): Deferred<Response<List<BookItem>>>

    @GET("http://52.196.162.105/api/profile/{user_id}")
    fun apiGetProfileInfo(
        @Path("user_id") userId: String,
        @Header("authorization") token: String?
    ): Deferred<Response<UserProfile>>

    @GET("http://52.196.162.105/api/books/category/{category_id}")
    fun apiGetCategoryBook(@Path("category_id") id: String): Deferred<List<BookItem>>

    @GET("http://52.196.162.105/api/books/{book_id}")
    fun apiGetSelectedBookDetail(@Path("book_id") bookId: Int?): Deferred<Response<selectedBook>>

}


object BookApi {

    private val BASE_URL = "http://52.196.162.105/api/"

//    private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()


    val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if(BuildConfig.DEBUG){HttpLoggingInterceptor.Level.BODY}else
        {HttpLoggingInterceptor.Level.NONE})


    val cacheSize = (10 * 1024 * 1024).toLong()
//    val chache = Cache(MainActivity().cacheDir, cacheSize)


    val client = OkHttpClient.Builder()
        //新增攔截器統一追加參數
        .addInterceptor(object : Interceptor {


            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val newRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Content-Type","multipart/form-data")
                    .build()


                return chain.proceed(newRequest)
            }
        })
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
//            .cache(chache)
        .build()


    val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .client(client)
        .build()


    val retrofitService = retrofit.create(BookApiService::class.java)


}
