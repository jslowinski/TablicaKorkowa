package com.example.tablicakorkowa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tablicakorkowa.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.databinding.DataBindingUtil.setContentView
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }



}
