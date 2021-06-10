package com.example.bookreports.registerinfo


import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentRegisterAreaBinding
import com.example.bookreports.utils.MainViewModel
import kotlinx.coroutines.*
import timber.log.Timber


class RegisterAreaFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentRegisterAreaBinding.inflate(inflater)
        val pb_register = binding.pbRegister
        val spinner = binding.spinnerArea
        val adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.spinner_area, R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter



        viewModel.registerAccount.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Timber.d("enter register account it:$it")
                this.findNavController().navigate(RegisterAreaFragmentDirections.actionRegisterAreaFragmentToHomeFragment())
            }
        })

        viewModel.registerError.observe(viewLifecycleOwner, Observer {
            if(it != null){
            Timber.d("enter register error it : $it")
            this.findNavController().navigate(RegisterAreaFragmentDirections.actionRegisterAreaFragmentToRegisterEMailFragment())
            Toast.makeText(requireActivity(), "我是 registerError ob${viewModel.registerError.value?.errors?.email}", Toast.LENGTH_SHORT).show()}

        })




        binding.btnAreaNext.setOnClickListener {

            val areaString = spinner.selectedItem.toString()
            if (areaString.length > 0) {
                Timber.d("enter >0")
                viewModel.list[4] = areaString
                if (viewModel.list.size == 5) {
                    Timber.d("enter list.size == 5")
                    viewModel.registerUser()
                    pb_register.visibility = View.VISIBLE
                }
            }
        }
        return binding.root
    }
}



