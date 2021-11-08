package com.example.bookreports.registerinfo

import android.accounts.Account
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentRegisterPassWordConfirmationBinding
import com.example.bookreports.utils.MainViewModel
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterPassWordConfirmationFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val vmAccount: AccountViewModel by sharedViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding = FragmentRegisterPassWordConfirmationBinding.inflate(inflater)

        val nav = this.findNavController()
        binding.btnPasswordNext1.setOnClickListener {
//            val password_confirmation = binding.editTextTextPassword1.text.toString()
//            if(viewModel.list[2] == password_confirmation){
//                viewModel.list[3] = password_confirmation
//                this.findNavController().navigate(RegisterPassWordConfirmationFragmentDirections.actionRegisterPassWordConfirmationFragmentToRegisterAreaFragment())
//            }else{
//                Toast.makeText(requireActivity(),"兩次輸入的密碼不一致，請再重新輸入一次",Toast.LENGTH_SHORT).show()
//            }
            val password_confirmation = binding.editTextTextPassword1.text.toString()
            if(vmAccount.list[2] == password_confirmation){
                vmAccount.list[3] = password_confirmation
                if (nav.currentDestination?.id == R.id.registerPassWordConfirmationFragment){
                    nav.navigate(R.id.registerAreaFragment)
                }
            }else{
                Toasty.warning(requireActivity(),"密碼不一致，請重新輸入",Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root

    }
}