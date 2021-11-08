package com.example.bookreports.registerinfo


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentRegisterAreaBinding
import com.example.bookreports.utils.MainViewModel
import com.example.bookreports.utils.SharedPreference
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class RegisterAreaFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val vmAccount: AccountViewModel by sharedViewModel()
    lateinit var binding: FragmentRegisterAreaBinding
    lateinit var pb_register: ProgressBar
    lateinit var spinner:Spinner

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val nav = this.findNavController()

        binding = FragmentRegisterAreaBinding.inflate(inflater)

        initUi()


        vmAccount.accountInfo.observe(viewLifecycleOwner, Observer {
            if (it != null && nav.currentDestination?.id == R.id.registerAreaFragment){
                nav.navigate(R.id.homeFragment)
                alertDialog()
            }
        })


//        viewModel.registerAccount.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                Timber.d("enter register account it:$it")
//                alertDialog()
//                this.findNavController().navigate(RegisterAreaFragmentDirections.actionRegisterAreaFragmentToHomeFragment())
//            }
//        })

//        viewModel.registerError.observe(viewLifecycleOwner, Observer {
//            if(it != null){
//            Timber.d("enter register error it : $it")
//            this.findNavController().navigate(RegisterAreaFragmentDirections.actionRegisterAreaFragmentToRegisterEMailFragment())
//            Toast.makeText(requireActivity(), "我是 registerError ob${viewModel.registerError.value?.errors?.email}", Toast.LENGTH_SHORT).show()}
//
//        } )

        vmAccount.registerFail.observe(viewLifecycleOwner, Observer {
            if (it.errors.email[0] == "The email has already been taken."){
                   Timber.d("emaillFail ${it.errors.email[0]}")
                Toast.makeText(requireActivity(),"信箱帳號重複，請輸入其他信箱",Toast.LENGTH_SHORT).show()
            }else if (it.errors.email[0] == "The email must be a valid email address."){
                Timber.d("emaillFail ${it.errors.email[0]}")
                Toast.makeText(requireActivity(),"Email 非有效的格式，請重新輸入",Toast.LENGTH_SHORT).show()
            }else{
                Timber.d("emaillFail ${it.errors.email[0]}")
                Timber.d("異常錯誤")
            }
            if (nav.currentDestination?.id == R.id.registerAreaFragment){
                nav.navigate(R.id.registerEMailFragment)
            }
        })




        binding.btnAreaNext.setOnClickListener {

            val areaString = spinner.selectedItem.toString()
//            if (areaString.length > 0) {
//                Timber.d("enter >0")
//                viewModel.list[4] = areaString
//                if (viewModel.list.size == 5) {
//                    Timber.d("enter list.size == 5")
//                    viewModel.registerUser()
//                    pb_register.visibility = View.VISIBLE
//                }
//            }

            if(areaString.isNotEmpty()){
                vmAccount.list[4] = areaString
                    vmAccount.registerAccount()
                    pb_register.visibility = View.VISIBLE
            }
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
                        sharedPreferences.savePassword(vmAccount.list[2])
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

    private fun initUi(){
        pb_register = binding.pbRegister
        spinner = binding.spinnerArea
        val adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.spinner_area, R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter

    }
}



