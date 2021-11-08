package com.example.bookreports.registerinfo



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentRegisterNameBinding
import com.example.bookreports.utils.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RegisterNameFragment : Fragment() {


    private val vmAccount: AccountViewModel by sharedViewModel()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegisterNameBinding.inflate(inflater)

        val nav = this.findNavController()

        val key = Regex("[a-z0-9A-Z\\u4e00-\\u9fa5]*")

        binding.btnNameNext.setOnClickListener {
            val name =binding.editName.text.toString()
            if (name.length > 0 && name.matches(key)) {
                vmAccount.list[0] = name
                if (nav.currentDestination?.id == R.id.registerNameFragment){
                    nav.navigate(RegisterNameFragmentDirections.actionRegisterNameFragmentToRegisterEMailFragment())
                }
            }else{
                Toast.makeText(requireActivity(),"不得填入為空白/亂碼/符",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}