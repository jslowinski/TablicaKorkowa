package com.example.tablicakorkowa


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tablicakorkowa.databinding.FragmentSingInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_sing_in.*
import timber.log.Timber


class SingInFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var auth: FirebaseAuth
    lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = activity?.let{
            GoogleSignIn.getClient(it, gso)
        }!!

//        val bindingg: MartianDataBinding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_sing_in, container, false
//        )
        val binding = FragmentSingInBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.googleButton.setOnClickListener {
            signIn()
        }

        binding.textView3.setOnClickListener { view: View ->
            view.findNavController().navigate(SingInFragmentDirections.actionSingInFragmentToSingUpFragment())
        }

        binding.forgotText.setOnClickListener { view: View ->
            view.findNavController().navigate(SingInFragmentDirections.actionSingInFragmentToForgotFragment())
        }


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sing_in, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.setOnClickListener {
            doLogin()
        }

        setGooglePlusButtonText(google_button, "Dołącz z Google")
    }


    private fun setGooglePlusButtonText(
        signInButton: SignInButton,
        buttonText: String?
    ) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (i in 0 until signInButton.childCount) {
            val v = signInButton.getChildAt(i)
            if (v is TextView) {
                v.text = buttonText
                return
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString() + "   " + requestCode)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it, OnCompleteListener<AuthResult>() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignInActivity", "signInWithCredential:success")
                        updateUI(auth.currentUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("SignInActivity", "signInWithCredential:failure")
                    }
                })
        }
    }

    private fun doLogin() {
        if (validateCorrectEmail() or validatePassword()){
            return
        }

        activity?.let {
            auth.signInWithEmailAndPassword(textInputLayout.editText?.text.toString(), textInputLayout2.editText?.text.toString())
                .addOnCompleteListener(it, OnCompleteListener<AuthResult>() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w(task.exception, "signInWithEmail:failure")
                        Toast.makeText(
                            context, "Błąd logowania",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                })
        }
    }

    //region validate



    private fun validateCorrectEmail(): Boolean{
        return if(!Patterns.EMAIL_ADDRESS.matcher(textInputLayout.editText?.text.toString()).matches()){
            textInputLayout.error = "Wprowadź poprawny adres email"
            true
        } else {
            textInputLayout.error = null
            false
        }
    }

    private fun validatePassword(): Boolean{
        return if(textInputLayout2.editText?.text.toString().isEmpty()){
            textInputLayout2.error = "Pole nie może być puste"
            true
        } else {
            textInputLayout2.error = null
            false
        }
    }

    override fun onStart() {
        val user = auth.currentUser
        updateUI(user)
        super.onStart()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(context,MainActivity::class.java))
            requireActivity().finish()
        }
    }

}