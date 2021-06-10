package com.example.bookreports.utils


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bookreports.BuildConfig
import com.example.bookreports.R
import com.example.bookreports.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class MainActivity : AppCompatActivity() {



    lateinit var navController :NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)









        //側邊menu
        val drawerLayout = findViewById<DrawerLayout>(R.id.layout_drawer)
        val navView = findViewById<NavigationView>(R.id.navigation_view)

        navController = findNavController(R.id.nav_host_fragment)


        //有沒有現在沒差
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homepage -> navController.navigate(R.id.homeFragment)
                R.id.login -> navController.navigate(R.id.loginFragment)
                R.id.setting -> navController.navigate(R.id.overViewFragment)
                R.id.profile -> navController.navigate(R.id.profileFragment)
                R.id.bookStore -> navController.navigate(R.id.bookStoreFragment)
                R.id.forMessage -> Toast.makeText(
                    this,
                    "Message功能開發中...",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.notify -> Toast.makeText(
                    this,
                    "通知功能開發中...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START,false)
            return@setNavigationItemSelectedListener true
        }


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.activity_main_drawer)
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.closeDrawer(navView)
            } else {
                drawerLayout.openDrawer(navView)
            }
        }

        val layou = window.decorView

        val connectState = checkConnectState(this)
        if (connectState == false){
            Snackbar.make(layou,"無法連線", Snackbar.LENGTH_LONG).setAction("我了解了"){
                it.setOnClickListener { Toast.makeText(this,"點到我囉",Toast.LENGTH_SHORT).show() }
            }.show()
        }



    }
    private fun checkConnectState(context: Context): Boolean {
        var result = false
        val connectManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectManager.getNetworkCapabilities(connectManager.activeNetwork)?.run {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        } else {
            val netInfo = connectManager.activeNetworkInfo
            result = netInfo?.isConnectedOrConnecting == true

        }
        return result

    }
}
