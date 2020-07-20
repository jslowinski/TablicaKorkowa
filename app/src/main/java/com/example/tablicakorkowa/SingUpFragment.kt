package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.tablicakorkowa.data.api.Users.RequestRegister
import com.example.tablicakorkowa.data.api.Users.ResponseUser
import com.example.tablicakorkowa.databinding.FragmentSingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.tablicakorkowa.helpers.*
import com.example.tablicakorkowa.viewmodel.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_sing_up.*
import kotlinx.android.synthetic.main.fragment_sing_up.textInputLayout
import kotlinx.android.synthetic.main.fragment_sing_up.textInputLayout2
import timber.log.Timber


class SingUpFragment : Fragment() {

    // Private Properties
    private lateinit var auth: FirebaseAuth
    private val viewModel by lazy { ViewModelProviders.of(this).get(SignUpViewModel::class.java) }
    private var disposable: Disposable? = null



    // View Life Cycle

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val binding = FragmentSingUpBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.signUpButton.setOnClickListener {
            signUpUser()
        }

        binding.signUpBackButton.setOnClickListener { view: View ->
            view.findNavController().navigate(SingUpFragmentDirections.actionSingUpFragmentToSingInFragment())
        }

        binding.signUpBackText.setOnClickListener { view: View ->
            view.findNavController().navigate(SingUpFragmentDirections.actionSingUpFragmentToSingInFragment())
        }


        return binding.root
    }

    override fun onPause() {
        disposable?.dispose()
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonEffect(signUpBackButton)
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


    private fun signUpUser(){
                if (validateCorrectEmail() or validateName() or validateLastName() or validatePassword()){
                    return
                }
                activity?.let {
                    auth.createUserWithEmailAndPassword(signUpEmail.text.toString(), signUpPassword.text.toString())
                        .addOnCompleteListener(it) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Timber.d("createUserWithEmail:success")
                                val user = auth.currentUser
                                Timber.d(user!!.uid)

                                val users = RequestRegister(
                                    signUpName.text.toString(),
                                    signUpLastname.text.toString(),
                                    if (signUpPhone.text.toString().isEmpty()) {
                                        ""
                                    } else {
                                        signUpPhone.text.toString()
                                    },
                                    signUpEmail.text.toString(),
                                    user.uid,
                                    "",
                                    "email"
                                )

                                bindRegisterUser(users)
                                bindUIData()


                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.w(task.exception, "createUserWithEmail:failure")
                                Toast.makeText(
                                    context, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

    }

    //endregion

    //region validate

    private fun bindRegisterUser(user: RequestRegister){
        viewModel.registerUser(user)
    }

    private fun bindUIData() {
        viewModel.user.subscribe(this, ::updateUserID)
        viewModel.errors.subscribe(this, ::showErrorMessage)
    }

    private fun updateUserID(user: ResponseUser){
        val sharedPreferences =
            this.requireActivity().getSharedPreferences("USERID", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ID", user.id)
        editor.apply()
        Timber.d(user.id)
    }

    private fun showErrorMessage(error: ErrorMessage) {
        Snackbar.make(rootSignUp, error.getMessage(), Snackbar.LENGTH_SHORT).show()
    }

    private fun validateCorrectEmail(): Boolean{
        return if(!Patterns.EMAIL_ADDRESS.matcher(signUpEmail.text.toString()).matches()){
            textInputLayout3.error = "Wprowadź poprawny adres email"
            true
        } else {
            textInputLayout.error = null
            false
        }
    }

    private fun validateName(): Boolean{
        return if(signUpName.text.toString().isEmpty()){
            textInputLayout.error = "Pole nie może być puste"
            true
        } else {
            textInputLayout.error = null
            false
        }
    }

    private fun validateLastName(): Boolean{
        return if(signUpLastname.text.toString().isEmpty()){
            textInputLayout2.error = "Pole nie może być puste"
            true
        } else {
            textInputLayout2.error = null
            false
        }
    }

    private fun validatePassword(): Boolean{
        return if(signUpPassword.text.toString().isEmpty()){
            textInputLayout4.error = "Pole nie może być puste"
            true
        } else {
            textInputLayout4.error = null
            false
        }
    }

    //endregion
}