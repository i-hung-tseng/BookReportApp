package com.example.bookreports.utils


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bookreports.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        if (getSupportActionBar() != null){
            getSupportActionBar()?.hide()
        }

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity,MainActivity::class.java )
            startActivity(intent)
            finish()
        },1000)

    }
}