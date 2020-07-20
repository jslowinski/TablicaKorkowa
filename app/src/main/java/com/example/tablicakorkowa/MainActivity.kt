package com.example.tablicakorkowa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences =
            this.getSharedPreferences("USERID", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("ID", "Brak ID")
        Timber.e(id)
        Timber.e(FirebaseAuth.getInstance().uid)
        singOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }
    }
}
