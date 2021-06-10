package com.example.bookreports.home.category

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentOverViewBusinessBinding
import com.example.bookreports.network.BookApi
import com.example.bookreports.utils.MainViewModel
import timber.log.Timber


class OverViewBusinessFragment : Fragment() {



    lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: MainViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentOverViewBusinessBinding.inflate(inflater)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerviewRecommend.adapter = BusinessAdapter(BusinessAdapter.OnClickListener{
            viewModel.addDetailBook(it)
            this.findNavController().navigate(R.id.detailFragment)
        })

        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerviewRecommend.layoutManager = linearLayoutManager


        if(viewModel.categoryBookList.value == null){
        viewModel.getCategoryBookListFromApi("1",null)}

        binding.btnRank.setOnClickListener {
            Log.d("Testing","btn ${viewModel.categoryBookList.value}")
            viewModel.getCategoryBookListFromApi("1","上架時間")
        }


//        viewModel.notificationBookCount.observe(viewLifecycleOwner, Observer {
//            if(it != null){
//                viewModel.notifyBookCount()
//            }
//        })

//        createChannel("book_channel","bookReport")

        return binding.root

    }

    //創建 channel 並給予 channelId，重要度
//    private fun createChannel(channelId:String, channelName:String){
//    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//        val notificationChannel = NotificationChannel(
//            channelId,
//            channelName,
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationChannel.enableLights(true)
//        notificationChannel.lightColor = Color.RED
//        notificationChannel.enableVibration(true)
//        notificationChannel.description = "通知目前新書數量"
//
//        val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
//        //創建一個剛剛設定好的 channel
//        notificationManager.createNotificationChannel(notificationChannel)
//
//    }
//
//
//
//    }
//
//
//
//
}