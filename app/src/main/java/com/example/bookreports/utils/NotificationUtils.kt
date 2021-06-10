//package com.example.bookreports.utils
//
//import android.app.Application
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import com.example.bookreports.R
//
//
//private val NOTIFICATION_ID = 0
//private val REQUEST_CODE = 0
//private val FLAGS = 0
//
//
//
//fun NotificationManager.sendNotifcation(messageBody: String,application: Context){
//
//    //點擊之後盡到activity
//    val contentIntent = Intent(application,MainActivity::class.java)
//
//
//    val contentPendingIntent = PendingIntent.getActivity(
//        application,
//        NOTIFICATION_ID,
//        contentIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT
//    )
//
//
//
//    val builder = Notification.Builder(application,"book_channel")
//
//
//            //圖示出現在右方
//            .setSmallIcon(R.drawable.vector)
//            //顯示在通知的上方
//            .setContentTitle("book Report")
//            //出現在下方
//            .setContentText(messageBody)
//
//            .setContentIntent(contentPendingIntent)
//            .setAutoCancel(true)
//
//
//
//
//        //產生到status bar 裡面，若已經有相同的id之後，但是仍然 notify之後，他會更新新的notification
//        notify(NOTIFICATION_ID,builder.build())
//}
//
//    fun NotificationManager.cancelNotifications(){
//        cancelAll()
//    }