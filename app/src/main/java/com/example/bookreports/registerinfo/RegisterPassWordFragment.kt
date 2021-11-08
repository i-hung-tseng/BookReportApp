package com.example.bookreports.registerinfo

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
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
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class RegisterPassWordFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val vmAccount: AccountViewModel by sharedViewModel()
    lateinit var passWord: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentRegisterPassWordBinding.inflate(inflater)
        val pb = binding.pbLogin
        val nav = this.findNavController()

//        viewModel.registerAccount.observe(viewLifecycleOwner, Observer {
//            if(it != null) {
//                this.findNavController().navigate(R.id.homeFragment)
//                alertDialog()
//            }
//        })
        
        
        vmAccount.accountInfo.observe(viewLifecycleOwner, Observer {
            if( it != null && nav.currentDestination?.id == R.id.registerPassWordFragment){
                nav.navigate(R.id.homeFragment)
                Timber.d("accountInfo : ${vmAccount.accountInfo.value}")
                alertDialog()
            }
        })

        // TODO: 2021/6/23 確認singlelivedata 有沒有用 
        vmAccount.loginFail.observe(viewLifecycleOwner, Observer { 
            if ( it.message == "Bad creds"){
                Toast.makeText(requireActivity(),"請再次確認帳號與密碼是否錯誤",Toast.LENGTH_SHORT).show()
                Timber.d("loginFail : ${vmAccount.loginFail.value}")
                nav.navigate(R.id.registerEMailFragment)
                
            }
        })
        
//
//        viewModel.loginFail.observe(viewLifecycleOwner, Observer {
//            if(it == "Bad creds"){
//                if (this.findNavController().currentDestination?.id ==  R.id.registerPassWordFragment){
//                    Toast.makeText(requireActivity(),"請確認帳號密碼是否正確",Toast.LENGTH_SHORT).show()
//                    this.findNavController().navigate(R.id.registerEMailFragment)
//                }
//            }
//        })



        binding.btnPasswordNext.setOnClickListener {
            passWord = binding.editTextTextPassword.text.toString()
            if (passWord.length >= 8 && nav.currentDestination?.id == R.id.registerPassWordFragment){
                    vmAccount.list[2] = passWord
                if (vmAccount.newOrNot.value == true){
                    nav.navigate(R.id.registerPassWordConfirmationFragment)
                }else{
                    vmAccount.login(vmAccount.list[1],passWord)
                    Timber.d("email: ${vmAccount.list[1]}")
                    pb.visibility = View.VISIBLE
                }
            }else{
                Toast.makeText(requireActivity(),"密碼請輸入大於8碼以上",Toast.LENGTH_SHORT).show()
            }







//            if (viewModel.newMemberOrAlreadyMember.value == "newMember") {
//                if (passWord.length >= 8) {
//                    viewModel.list[2] = passWord
//                    this.findNavController().navigate(RegisterPassWordFragmentDirections.actionRegisterPassWordFragmentToRegisterPassWordConfirmationFragment())
//                } else {
//                    Toast.makeText(requireActivity(), "密碼請輸入大於8號碼以上", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                viewModel.login(viewModel.list[1], passWord)
//                pb.visibility = View.VISIBLE
//
//
//
//            }
        }






        return binding.root
    }

    private fun alertDialog(){

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("記住帳號?")
                .setMessage("把帳號密碼記住，下次就會自動登入囉!")
                .setPositiveButton("好的!",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        //存取本地端資料
                        val sharedPreferences = SharedPreference(requireActivity())
                        sharedPreferences.saveEmail(vmAccount.list[1])
                        Timber.d("記住帳密 ${vmAccount.list[1]}")
                        sharedPreferences.savePassword(passWord)
                        sharedPreferences.saveRememberAccount(true)
                        sharedPreferences.saveState(true)
                    }

                })
                .setNegativeButton("下次再說!", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Timber.d("沒有記住")
                        val sharedPreferences = SharedPreference(requireActivity())
                        sharedPreferences.saveRememberAccount(false)

                    }

                })
        builder.show()


    }

}