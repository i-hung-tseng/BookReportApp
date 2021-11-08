package com.example.bookreports.registerinfo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentRegisterEMailBinding
import com.example.bookreports.utils.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.regex.Pattern


class RegisterEMailFragment : Fragment(){

    private val vmAccount: AccountViewModel by sharedViewModel()

    lateinit var binding: FragmentRegisterEMailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterEMailBinding.inflate(inflater)

        // TODO: 2021/6/23 確認是否需要這個
//        viewModel.resetRegisterError()
//        viewModel.resetLoginFail()

        val nav = this.findNavController()

        binding.btnEmailNext.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            // \w\ 包含數字字母下底線      \避開特殊字元 這邊如果用 tina的不行
            // TODO: 2021/6/23 這邊如果用tina的話不行，要再確認
//            val key = Regex("/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})\$/")
            val key = Regex("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?")
            val test = email.matches(key)
            checkEmail()
            Timber.d("checkEmail: ${checkEmail()}")
            Timber.d("test: $test")
//            Timber.d("email.length: ${email.length}")
//            Timber.d("nav : ${getBoolean()}")
            val emailResult = checkEmail()
            Timber.d("email.length: ${email.length}")
            Timber.d("nav.curId ${nav.currentDestination?.id}")
            if (email.length > 0 && emailResult && nav.currentDestination?.id == R.id.registerEMailFragment) {
                vmAccount.list[1] = email
                this.findNavController().navigate(RegisterEMailFragmentDirections.actionRegisterEMailFragmentToRegisterPassWordFragment())
            }else{
                Toast.makeText(requireActivity(),"請輸入正確的Email格式",Toast.LENGTH_SHORT).show()
            }
        }









        return binding.root
    }

//    private fun getBoolean():Boolean{
//        if(this.findNavController().currentDestination?.id == R.id.registerEMailFragment){
//            return true
//        }else{
//            return false
//        }
//    }
//
//    private fun autoCompleteEmail(){
//
//        val autoTextView = binding.editTextTextEmailAddress
//        val endOfEmail = resources.getStringArray(R.array.email)
//        val adapter = ArrayAdapter(requireActivity(),android.R.layout.simple_list_item_1,endOfEmail)
//        autoTextView.setAdapter(adapter)
//        autoTextView.threshold = 1
//        val enterText = autoTextView.text.toString()
//        Timber.d("enterText $enterText")
//
//    }


    private fun checkEmail(): Boolean{
        val email = binding.editTextTextEmailAddress.text.toString()
        val regex =  "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?"
        return Pattern.matches(regex,email)
    }
}