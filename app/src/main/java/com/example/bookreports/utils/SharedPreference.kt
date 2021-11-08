package com.example.bookreports.utils

import android.content.Context


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

    fun saveRememberAccount(rememberOrNot: Boolean){
        sharedPreference.edit().putBoolean("記住帳密",rememberOrNot).apply()
    }

    fun getRememberAccount(): Boolean{
        return sharedPreference.getBoolean("記住帳密",false)
    }

    fun saveState(loginOrNot: Boolean){
        sharedPreference.edit().putBoolean("維持",loginOrNot).apply()
    }

    fun getState(): Boolean{
        return sharedPreference.getBoolean("維持",true)
    }
}