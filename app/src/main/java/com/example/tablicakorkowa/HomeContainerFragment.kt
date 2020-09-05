package com.example.tablicakorkowa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tablicakorkowa.databinding.FragmentHomeContainerBinding
import timber.log.Timber


class HomeContainerFragment : Fragment() {

    private lateinit var binding: FragmentHomeContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

}