package com.example.bookreports.utils

import android.content.Context
import android.util.Log

class SharedPreference(content: Context) {

    private val sharedPreference = content.getSharedPreferences("帳密",0)

    fun saveEmail(email: String){
        sharedPreference.edit().putString("信箱",email).apply()
    }

    fun savePassword(pass: String){
        sharedPreference.edit().putString("密碼",pass).apply()
    }

    fun getEmail(): String? {
        return sharedPreference.getString("信箱","")
    }
    fun getPass(): String?{
        return sharedPreference.getString("密碼","")
    }
}