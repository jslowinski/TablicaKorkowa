package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.tablicakorkowa.SignInActivity.Companion.auth
import com.example.tablicakorkowa.databinding.FragmentForgotBinding
import com.example.tablicakorkowa.helpers.buttonEffect
import kotlinx.android.synthetic.main.fragment_forgot.*

class ForgotFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentForgotBinding.inflate(inflater, container, false)

        binding.forgotButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        binding.buttonReset.setOnClickListener {view: View ->
            when(binding.buttonReset.text) {
                "Wyślij" -> {
                    doReset()
                }
                else -> {
                    view.findNavController().navigate(ForgotFragmentDirections.actionForgotFragmentToSingInFragment())
                }
            }
        }


        binding.forgotSingIn.setOnClickListener {view: View ->
            when(binding.forgotSingIn.text) {
                "Wyślij" -> {
                    doReset()
                }
                else -> {
                    view.findNavController().navigate(ForgotFragmentDirections.actionForgotFragmentToSingInFragment())
                }
            }
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonEffect(forgotButton)
    }

    private fun doReset(){
        if (validateCorrectEmail())
            return

        auth.sendPasswordResetEmail(resetField.editText?.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateUI()
                }
            }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        imageView.setImageResource(R.drawable.forgot_password_send_img)
        forgotTitle.text = "Email został wysłany"
        forgotDesc.text = "Sprawdź swoją skrzynkę pocztową i kliknij\n" +
                " w link do odzyskiwania hasła"
        buttonReset.text = "Zaloguj"
        textView6.text = "Mail nie dotarł?"
        forgotSingIn.text = "Ponów"
        resetField.visibility = View.GONE
    }

    private fun validateCorrectEmail(): Boolean {
        return if(!Patterns.EMAIL_ADDRESS.matcher(resetField.editText?.text.toString()).matches()){
            resetField.error = "Wprowadź poprawny adres email"
            true
        } else {
            resetField.error = null
            false
        }
    }


}