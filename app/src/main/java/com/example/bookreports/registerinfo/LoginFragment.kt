package com.example.bookreports.registerinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookreports.databinding.FragmentLoginBinding
import com.example.bookreports.utils.MainViewModel


class LoginFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater)
        val pb_logout = binding.pbLogout



        initUi()


        viewModel.logOutState.observe(viewLifecycleOwner, Observer {
            if (it != null){
                if (it == "Logged out."){
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    Toast.makeText(requireActivity(),"登出成功",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireActivity(),"登出失敗: cause:${viewModel.logOutState}",Toast.LENGTH_SHORT).show()
                    pb_logout.visibility = View.GONE
                }
            }
            viewModel.resetLogOut()
        })

        // TODO: 2021/5/26 簡易登入，之後要刪掉
        binding.btnEasylog.setOnClickListener {
            viewModel.login("iu@gmail.com", "iu")
        }


        viewModel.defineNewMemberOrAlreadyMember(null)


        binding.btnLogout.setOnClickListener {
            pb_logout.visibility = View.VISIBLE
            viewModel.logOut()
        }


        binding.btnLogin.setOnClickListener {
            viewModel.defineNewMemberOrAlreadyMember("member")
            this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterEMailFragment())
        }

        binding.btnRegister.setOnClickListener {
            viewModel.defineNewMemberOrAlreadyMember("newMember")
            this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterNameFragment())
        }



        return binding.root
    }

    private fun initUi(){
        if (viewModel.registerAccount.value?.user?.name != null) {
            binding.btnLogin.visibility = View.GONE
            binding.btnRegister.visibility = View.GONE
            binding.btnLogout.visibility = View.VISIBLE
        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
        }
    }
}
