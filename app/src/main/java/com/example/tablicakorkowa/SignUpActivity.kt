package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.example.tablicakorkowa.data.api.ApiClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private var name = ""
    private var lastname = ""
    private var email = ""
    private var phone = ""
    private var accountID = ""
    private var avatar = ""

    private val TAG = SignUpActivity::class.java.simpleName

    private lateinit var auth: FirebaseAuth

    private val apiService by lazy {
        ApiClient.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        auth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

//        signUpButton.setOnClickListener {
//            signUpUser()
//        }
//
//        signUpBackButton.setOnClickListener {
//            onBackPressed()
//        }
//
//        textView5.setOnClickListener {
//            onBackPressed()
//        }



//        val imageView:ImageView = findViewById(R.id.signUpAvatar)
//        Glide.with(this).load(R.drawable.user_avatar).apply(RequestOptions.circleCropTransform()).into(imageView)
//
//        buttonEffect(signUpBackButton)
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

    //region signUp

//    private fun signUpUser(){
//        if (validateCorrectEmail() or validateName() or validateLastName() or validatePassword()){
//            return
//        }
//
//        auth.createUserWithEmailAndPassword(signUpEmail.text.toString(), signUpPassword.text.toString())
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    Log.d(TAG, user!!.uid)
//
//                    name = signUpName.text.toString()
//                    lastname = signUpLastname.text.toString()
//                    email = signUpEmail.text.toString()
//                    accountID = user.uid
//                    phone = if (signUpPhone.text.toString().isEmpty()){
//                        ""
//                    } else {
//                        signUpPhone.text.toString()
//                    }
//
//                    val users = RequestRegister(name, lastname, phone, email, accountID, avatar)
//
//                    apiService
//                        .register(users)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe (
//                            {result ->
//                                Log.v("POST SUCCESS", "" + result.id)
//                                startActivity(Intent(this,MainActivity::class.java))
//                                finish()},
//                            {error -> Log.e("ERROR", error.message)}
//                        )
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    //endregion
//
//    //region validate
//
//    private fun validateCorrectEmail(): Boolean{
//        return if(!Patterns.EMAIL_ADDRESS.matcher(signUpEmail.text.toString()).matches()){
//            textInputLayout3.error = "Wprowadź poprawny adres email"
//            true
//        } else {
//            textInputLayout.error = null
//            false
//        }
//    }
//
//    private fun validateName(): Boolean{
//        return if(signUpName.text.toString().isEmpty()){
//            textInputLayout.error = "Pole nie może być puste"
//            true
//        } else {
//            textInputLayout.error = null
//            false
//        }
//    }
//
//    private fun validateLastName(): Boolean{
//        return if(signUpLastname.text.toString().isEmpty()){
//            textInputLayout2.error = "Pole nie może być puste"
//            true
//        } else {
//            textInputLayout2.error = null
//            false
//        }
//    }
//
//    private fun validatePassword(): Boolean{
//        return if(signUpPassword.text.toString().isEmpty()){
//            textInputLayout4.error = "Pole nie może być puste"
//            true
//        } else {
//            textInputLayout4.error = null
//            false
//        }
//    }

    //endregion
}
