package com.example.bookreports.registerinfo

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentRegisterPassWordBinding
import com.example.bookreports.utils.MainViewModel
import com.example.bookreports.utils.SharedPreference
import timber.log.Timber


class RegisterPassWordFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentRegisterPassWordBinding.inflate(inflater)
        val pb = binding.pbLogin


        viewModel.registerAccount.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(R.id.homeFragment)
            }
        })




        binding.btnPasswordNext.setOnClickListener {
            val passWord = binding.editTextTextPassword.text.toString()
            if (viewModel.newMemberOrAlreadyMember.value == "newMember") {
                if (passWord.length >= 8) {
                    viewModel.list[2] = passWord
                    this.findNavController().navigate(RegisterPassWordFragmentDirections.actionRegisterPassWordFragmentToRegisterPassWordConfirmationFragment())
                } else {
                    Toast.makeText(requireActivity(), "密碼請輸入大於8號碼以上", Toast.LENGTH_SHORT).show()
                }
            } else {
                viewModel.login(viewModel.list[1], passWord)
                pb.visibility = View.VISIBLE
                //存取本地端資料
                val sharedPreferences = SharedPreference(requireActivity())
                sharedPreferences.saveEmail(viewModel.list[1])
                sharedPreferences.savePassword(passWord)
            }
        }






        return binding.root
    }

}