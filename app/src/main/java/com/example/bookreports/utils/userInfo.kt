package com.example.bookreports.utils

import android.accounts.Account
import androidx.fragment.app.Fragment
import com.example.bookreports.registerinfo.AccountViewModel
import org.koin.androidx.viewmodel.compat.SharedViewModelCompat.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class userInfo(): Fragment(){

    private val vmAccount: AccountViewModel by sharedViewModel()

    val id = vmAccount.accountInfo.value?.user?.id.toString()
    val token = "Bearer " + vmAccount.accountInfo.value?.token

}

