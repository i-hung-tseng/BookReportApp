package com.example.bookreports.home.category

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentOverViewHealthBinding
import timber.log.Timber


class OverViewHealthFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentOverViewHealthBinding.inflate(inflater)


        val str1 = "啊！多麼痛的領悟 你曾是我的全部"
        val str2 = "只是我回首來時路的每一步都走的好孤獨"
        val str3 = "啊！多麼痛的領悟 你曾是我的全部~~"
        val str4 = "只願你掙脫情的枷鎖 愛的束縛 任意追逐"
        val str5 = "別再為愛受苦"


        val picture = BitmapFactory.decodeResource(context?.resources,R.drawable.vector)


        val tv = binding.tvStory
        val span = SpannableString(str1)
        span.setSpan(RelativeSizeSpan(1.5F),0,3,Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        tv.text= span









        return binding.root
    }

    private fun setText(){



    }

}