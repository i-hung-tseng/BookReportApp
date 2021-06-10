package com.example.bookreports.overview



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookreports.databinding.FragmentOverViewBinding


class OverViewFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentOverViewBinding.inflate(inflater)


        return binding.root



    }
}