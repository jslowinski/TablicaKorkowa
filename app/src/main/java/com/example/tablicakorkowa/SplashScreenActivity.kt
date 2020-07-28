package com.example.tablicakorkowa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.util.Pair
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {

    //Variables
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    private val splashTime = 1500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)


        splash_logo.animation = topAnim
        splash_text.animation = bottomAnim

        val auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            updateUI(auth.currentUser)
        }, splashTime.toLong())
    }


    private fun updateUI(user: FirebaseUser?) {
        when (user) {
            null -> {
                val detailIntent = Intent(this, SignInActivity::class.java)
                val pairImageView = Pair.create<View, String>(splash_logo, "logoImage")
                val pairTextView = Pair.create<View, String>(splash_text, "logoText")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairImageView, pairTextView)
                startActivity(detailIntent, options.toBundle())
                finish()
            }
            else -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}