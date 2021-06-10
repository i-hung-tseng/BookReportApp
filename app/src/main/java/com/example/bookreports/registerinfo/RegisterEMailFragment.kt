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
import timber.log.Timber
import java.util.regex.Pattern


class RegisterEMailFragment : Fragment(){


    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegisterEMailBinding.inflate(inflater)

        viewModel.resetRegisterError()

        binding.btnEmailNext.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
//            val key = "[a-z]"
//            val pat = Pattern.compile(key)
//            val matcher = pat.matcher(email)
//
//            Timber.d("email check ${matcher.matches()}")

            // TODO: 2021/6/8 這邊之後要再次確認 
            val key = Regex("[a-z0-9A-Z]*+@+[a-z0-9A-Z._]*+[a-z0-9A-Z._]*+[a-z0-9A-Z._]*")
            if (email.length > 0 && email.matches(key) ) {
                viewModel.list[1] = email
                this.findNavController().navigate(RegisterEMailFragmentDirections.actionRegisterEMailFragmentToRegisterPassWordFragment())
            }else{
                Toast.makeText(requireActivity(),"請輸入正確的Email格式",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}