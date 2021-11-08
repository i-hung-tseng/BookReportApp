package com.example.bookreports.home


import VPAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.bookreports.databinding.FragmentHomeBinding
import com.example.bookreports.registerinfo.AccountViewModel
import com.example.bookreports.utils.MainViewModel
import com.example.bookreports.utils.SharedPreference
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.lang.reflect.Type


class HomeFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()
    private val vmAccount: AccountViewModel by sharedViewModel()
    private val vmBook: BookViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater)
//        if (viewModel.registerAccount.value?.token != null ) {
//            Toast.makeText(
//                requireActivity(),
//                "歡迎回來! ${viewModel.registerAccount.value?.user?.name}",
//                Toast.LENGTH_SHORT
//            ).show()
//            Log.d("Testing", "result body = ${viewModel.registerAccount.value?.user?.name}")
//        }


//                viewpager2
        val adapter = VPAdapter(childFragmentManager, lifecycle)
        val tablayout = binding.tabLayout
        val viewPager = binding.viewPager2
        viewPager.setUserInputEnabled(true)
        viewPager.adapter = adapter


        viewModel.resetGoBack()

        viewModel.setRateNum(0f)


        // TODO: 2021/6/29 這邊之後應該要改成 有變化才改，而不是透過 Null 
        vmAccount.accountInfo.observe(viewLifecycleOwner, Observer {
            if (it != null && vmAccount.profileInfo.value == null){
                val token = "Bearer " + it.token
                getProfileInfo(it.user.id.toString(),token)
                Timber.d("enter accountInfo observe")
            }
        })




        val title = arrayListOf<String>(
            "推薦",
            "商業",
            "養身",
            "人文",
            "職場",
        )
        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            tab.text = title[position]
            viewPager.setCurrentItem(tab.position, true)
        }.attach()


//        if (viewModel.firstTimeLogin.value == true) {
//            loginAccountFirst()
//            viewModel.resetFirestLogin()
//        }


        val sharedPreference = SharedPreference(requireActivity())
        val rememberOrNot = sharedPreference.getRememberAccount()
        Timber.d("rememberOrNot : $rememberOrNot")
        val state = sharedPreference.getState()
        if (rememberOrNot == true && state == true ){
            loginAccountFirst()
            Timber.d("logout :${viewModel.logOutState.value}")
            viewModel.resetFirestLogin()
        }





        return binding.root
    }

    private fun loginAccountFirst() {

        val sharedPreference = SharedPreference(requireActivity())
        val email = sharedPreference.getEmail()
        val password = sharedPreference.getPass()

        if (email != null || password != null) {
            vmAccount.login(email!!, password!!)
        }
    }

    private fun getProfileInfo(userId: String,token: String){
        vmAccount.getProfileInfo(userId,token)
    }



}
