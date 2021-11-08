package com.example.bookreports.utils


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.ActivityMainBinding
import com.example.bookreports.home.BookViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.mancj.materialsearchbar.MaterialSearchBar
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {



    lateinit var vmBook:BookViewModel
    lateinit var navController :NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        vmBook = getViewModel()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val searchBar = findViewById<MaterialSearchBar>(R.id.topSearchBar)


        if(Intent.ACTION_SEARCH == intent.action){
            Timber.d("query01 :enter query")
            intent.getStringExtra(SearchManager.QUERY)?.also {
                query -> Timber.d("query01:$query")
            }
        }




        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {
            }


            override fun onSearchStateChanged(enabled: Boolean) {
            }

            override fun onSearchConfirmed(text: CharSequence?) {
            if (text.isNullOrEmpty()){
                Toast.makeText(this@MainActivity,"請輸入關鍵字",Toast.LENGTH_SHORT).show()
            }else{
                Timber.d("text $text")
            vmBook.searchByBookNameFromApi(text.toString())
            }
            }
        })



        //側邊menu
        val drawerLayout = binding.layoutDrawer
        val navView = findViewById<NavigationView>(R.id.navigation_view)

        navController = findNavController(R.id.nav_host_fragment)


        //有沒有現在沒差
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homepage -> navController.navigate(R.id.homeFragment)
                R.id.login -> navController.navigate(R.id.loginFragment)
                R.id.profile -> navController.navigate(R.id.profileFragment)

            }
            drawerLayout.closeDrawer(GravityCompat.START,false)
            return@setNavigationItemSelectedListener true
        }


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        //以下是在右上方的 overflowmenu
//        toolbar.inflateMenu(R.menu.activity_main_drawer)

        //開啟左方的sideMenu跟關起
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.closeDrawer(navView)
            } else {
                drawerLayout.openDrawer(navView)
            }
        }

        val layou = window.decorView

        val connectState = checkConnectState(this)
        if (!connectState){
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
