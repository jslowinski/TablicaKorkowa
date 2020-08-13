package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.tablicakorkowa.SignInActivity.Companion.auth
import com.example.tablicakorkowa.data.api.model.RequestRegister
import com.example.tablicakorkowa.data.api.model.ResponseUser
import com.example.tablicakorkowa.databinding.FragmentSingUpBinding
import com.example.tablicakorkowa.helpers.*
import com.example.tablicakorkowa.viewmodel.Register
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_sing_up.*
import kotlinx.android.synthetic.main.fragment_sing_up.textInputLayout
import kotlinx.android.synthetic.main.fragment_sing_up.textInputLayout2
import timber.log.Timber


class SingUpFragment : Fragment() {

    // Private Properties
    private val viewModel by lazy { ViewModelProviders.of(this).get(Register::class.java) }
    private var disposable: Disposable? = null



    // View Life Cycle

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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


    // Validate TextFields
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
}