package com.example.bookreports.registerinfo



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bookreports.databinding.FragmentRegisterNameBinding
import com.example.bookreports.utils.MainViewModel


class RegisterNameFragment : Fragment() {



    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegisterNameBinding.inflate(inflater)




        binding.btnNameNext.setOnClickListener {
            val name =binding.editName.text.toString()
            if (name.length > 0) {
                viewModel.list[0] = name
                this.findNavController().navigate(RegisterNameFragmentDirections.actionRegisterNameFragmentToRegisterEMailFragment())

            }else{
                Toast.makeText(requireActivity(),"別忘記輸入您的姓名",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}