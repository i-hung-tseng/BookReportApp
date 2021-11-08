//package com.example.bookreports.utils
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.Dialog
//import android.content.DialogInterface
//import android.os.Bundle
//import androidx.fragment.app.DialogFragment
//import java.lang.IllegalStateException
//
//class AskRememberAccountDialogFragment: DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            builder.setMessage("是否記住帳密，以便下次登入")
//                    .setPositiveButton("好的",DialogInterface.OnClickListener { dialog, id ->
//
//                    })
//                    .setNegativeButton("不同意",DialogInterface.OnClickListener { dialog, id ->
//
//                    })
//            builder.create()
//        } ?: throw IllegalStateException ("Activity cannot be null")
//
//        }
//
//    }