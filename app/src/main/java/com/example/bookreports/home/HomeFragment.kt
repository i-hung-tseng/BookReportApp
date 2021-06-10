package com.example.bookreports.home


import VPAdapter
import android.content.Context
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
import com.example.bookreports.utils.MainViewModel
import com.example.bookreports.utils.SharedPreference
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import java.lang.reflect.Type


class HomeFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater)
        if (viewModel.registerAccount.value?.token != null) {
            Toast.makeText(
                requireActivity(),
                "歡迎回來! ${viewModel.registerAccount.value?.user?.name}",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("Testing", "result body = ${viewModel.registerAccount.value?.user?.name}")
        }


//                viewpager2
        val adapter = VPAdapter(childFragmentManager, lifecycle)
        val tablayout = binding.tabLayout
        val viewPager = binding.viewPager2
        viewPager.setUserInputEnabled(true)
        viewPager.adapter = adapter


        viewModel.resetGoBack()

        viewModel.setRateNum(0f)


        viewModel.registerAccount.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.getUserInfoFromApi()
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


        if (viewModel.firstTimeLogin.value == true) {
            loginAccountFirst()
            viewModel.resetFirestLogin()
        }






        return binding.root
    }

    private fun loginAccountFirst() {

        val sharedPreference = SharedPreference(requireActivity())
        val email = sharedPreference.getEmail()
        val password = sharedPreference.getPass()

        if (email != null || password != null) {
            Log.d("Testing1", "email & pass $email $password")
            viewModel.login(email!!, password!!)
        }
    }





}
