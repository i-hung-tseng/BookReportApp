package com.example.bookreports.data.register

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class registerUser(
    @SerializedName("name")
    var name:String? ,
    @SerializedName("email")
    var email:String? ,
    @SerializedName("password")
    var password:String?,
    @SerializedName("password_confirmation")
    var password_confirmation:String,
    @SerializedName("region")
    var region:String?
)

