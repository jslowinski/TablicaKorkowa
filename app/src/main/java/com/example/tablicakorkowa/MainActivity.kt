package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.example.tablicakorkowa.databinding.ActivityMainBinding
import com.example.tablicakorkowa.navigation.KeepStateNavigator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        // get fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!

        // setup custom navigator
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator

        // set navigation graph
        navController.setGraph(R.navigation.nav_graph)

        binding.bottomNav.setupWithNavController(navController)
    }
}

