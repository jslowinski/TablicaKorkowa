package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import com.example.tablicakorkowa.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val TAG = ForgotActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        auth = FirebaseAuth.getInstance()

        forgotBackButton.setOnClickListener {
            onBackPressed()
        }

        buttonReset.setOnClickListener {
            when (buttonReset.text) {
                "Wyślij" -> {
                    doReset()
                }
                else -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
        }

        textView5.setOnClickListener {
            when(textView5.text){
                "Zaloguj się" -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
                else -> {
                    doReset()
                }
            }
        }

        buttonEffect(forgotBackButton)
    }

    private fun doReset(){
        if (validateCorrectEmail())
            return

        auth.sendPasswordResetEmail(resetEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUI()
                }
            }
    }

    private fun updateUI(){
        textView9.text = "Email został wysłany!"
        textView10.text = "Sprawdź swoją skrzynkę pocztową i kliknij\n w link do odzyskiwania hasła"
        buttonReset.text = "Zaloguj"
        textView4.text = "Mail nie dotartł?"
        textView5.text = "Ponów"
        resetField.visibility = View.GONE
    }

    private fun validateCorrectEmail(): Boolean{
        return if(!Patterns.EMAIL_ADDRESS.matcher(resetEmail.text.toString()).matches()){
            resetField.error = "Wprowadź poprawny adres email"
            true
        } else {
            resetField.error = null
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonEffect(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
    }
}
