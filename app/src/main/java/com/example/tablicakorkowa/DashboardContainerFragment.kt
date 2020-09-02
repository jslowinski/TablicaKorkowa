package com.example.tablicakorkowa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tablicakorkowa.databinding.FragmentDashboardContainerBinding


class DashboardContainerFragment : Fragment() {

    private lateinit var binding: FragmentDashboardContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeContainerFragment", "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDashboardContainerBinding.inflate(inflater, container, false)
        return binding.root
    }
}
