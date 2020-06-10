package com.example.tablicakorkowa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class SignInActivity : AppCompatActivity() {

    private val TAG = SignInActivity::class.java.simpleName

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        textView3.setOnClickListener {
            val signUp = Intent(this, SignUpActivity::class.java)
            startActivity(signUp)
        }

        signInButton.setOnClickListener {
            doLogin()
        }

        forgotText.setOnClickListener {
            startActivity(Intent(this, ForgotActivity::class.java))
        }
    }


    private fun doLogin() {
        if (validateCorrectEmail() or validatePassword()){
            return
        }

        auth.signInWithEmailAndPassword(signInEmail.text.toString(), signInPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Błąd logowania",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

            }
    }

    //region validate

    private fun validateCorrectEmail(): Boolean{
        return if(!Patterns.EMAIL_ADDRESS.matcher(signInEmail.text.toString()).matches()){
            textInputLayout.error = "Wprowadź poprawny adres email"
            true
        } else {
            textInputLayout.error = null
            false
        }
    }

    private fun validatePassword(): Boolean{
        return if(signInPassword.text.toString().isEmpty()){
            textInputLayout2.error = "Pole nie może być puste"
            true
        } else {
            textInputLayout2.error = null
            false
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }


}
