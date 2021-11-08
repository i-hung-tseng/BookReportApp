package com.example.bookreports.registerinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentLoginBinding
import com.example.bookreports.utils.MainViewModel
import com.example.bookreports.utils.SharedPreference
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class LoginFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentLoginBinding
    lateinit var pb_logout: ProgressBar
    private val vmAccount: AccountViewModel by sharedViewModel()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater)

        val pb_logout = binding.pbLogout
        val nav = this.findNavController()






        initUi()


//        viewModel.logOutState.observe(viewLifecycleOwner, Observer {
//            if (it != null){
//                if (it == "Logged out."){
//                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
//                    Toast.makeText(requireActivity(),"登出成功",Toast.LENGTH_SHORT).show()
//                    val sharedPreference = SharedPreference(requireActivity())
//                    sharedPreference.saveState(false)
//
//                }else{
//                    Toast.makeText(requireActivity(),"登出失敗: cause:${viewModel.logOutState}",Toast.LENGTH_SHORT).show()
//                    pb_logout.visibility = View.GONE
//                }
//            }
//            viewModel.resetLogOut()
//        })


        vmAccount.logOutState.observe(viewLifecycleOwner, Observer {
            if ( it != null){
                Timber.d("it != null")
                if (it.message == "Logged out." && this.findNavController().currentDestination?.id == R.id.loginFragment  ){
                    nav.navigate(R.id.homeFragment)
                    Toast.makeText(requireActivity(),"登出成功",Toast.LENGTH_SHORT).show()
                    val sharedPreference = SharedPreference(requireActivity())
                    sharedPreference.saveState(false)
                    vmAccount.resetAccountInfo()
                }else{
                    Toast.makeText(requireActivity(),"登出失敗: 請確認是否已登入",Toast.LENGTH_SHORT).show()
                }
            }
            pb_logout.visibility = View.GONE

        })



//        // TODO: 2021/5/26 簡易登入，之後要刪掉
//        binding.btnEasylog.setOnClickListener {
//            viewModel.login("iu@gmail.com", "iu")
//        }


//        viewModel.defineNewMemberOrAlreadyMember(null)


        binding.btnLogout.setOnClickListener {
            pb_logout.visibility = View.VISIBLE
//            viewModel.logOut()
            vmAccount.logOut()
        }




        binding.btnLogin.setOnClickListener {
//            viewModel.defineNewMemberOrAlreadyMember("member")
//            this.findNavController()
//                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterEMailFragment())
            vmAccount.setNewOrNot(false)
            if (nav.currentDestination?.id == R.id.loginFragment ){
                nav.navigate(R.id.registerEMailFragment)
            }
        }

        binding.btnRegister.setOnClickListener {
//            viewModel.defineNewMemberOrAlreadyMember("newMember")
//            this.findNavController()
//                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterNameFragment())
            vmAccount.setNewOrNot(true)
            if (nav.currentDestination?.id == R.id.loginFragment){
                nav.navigate(R.id.registerNameFragment)
            }
        }

        return binding.root
    }

    private fun initUi(){
            pb_logout = binding.pbLogout
        Timber.d("enter initUi ${vmAccount.accountInfo.value?.token}")

        if (
//            viewModel.registerAccount.value?.user?.name != null
           vmAccount.accountInfo.value != null
        ) {
            Timber.d("ui not Null")
            binding.btnLogin.visibility = View.GONE
            binding.btnRegister.visibility = View.GONE
            binding.btnLogout.visibility = View.VISIBLE
        } else {
            Timber.d("ui is null")
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
        }
    }
}
