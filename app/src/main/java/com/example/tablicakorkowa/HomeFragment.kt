package com.example.tablicakorkowa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tablicakorkowa.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeFragment", "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.handler = this
        return binding.root
    }

    fun navigateToDetail(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(context, SignInActivity::class.java))
        requireActivity().finish()

    }

    fun showId(view: View) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Timber.e(uid)
    }
}