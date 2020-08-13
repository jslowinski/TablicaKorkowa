package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.tablicakorkowa.data.api.model.profile.UpdateData
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.databinding.FragmentProfilBinding
import com.example.tablicakorkowa.helpers.*
import com.example.tablicakorkowa.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_profil.*
import timber.log.Timber


class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding

    private val viewModel by lazy { ViewModelProviders.of(this).get(ProfileViewModel::class.java) }

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("NotificationsFragment", "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)

        bindUIData()
        bindUIUser()

        binding.profileSaveButton.setOnClickListener{
            updateUserAction()
        }

        binding.profileCancelButton.setOnClickListener {
            cancelUpdateUser()
        }


        return binding.root
    }


    private fun bindUIData() {
        viewModel.user.subscribe(this, ::showUserData)
        viewModel.error.subscribe(this, ::showErrorMessage)
    }

    @SuppressLint("SetTextI18n")
    private fun showUserData(user: UserDto){
        profileEditEmail?.editText?.setText(user.email)
        profileEditPhone?.editText?.setText(user.telephone)
        profileEditName?.editText?.setText(user.firstname)
        profileEditLastName?.editText?.setText(user.lastname)
        Picasso.get().load(user.avatar).into(profileImage)
        profileName.text = "${user.firstname} ${user.lastname}"
    }

    private fun bindUIUser() {
        viewModel.getUserProfile(getUid())
    }

    private fun showErrorMessage(error: ErrorMessage) {
        Timber.e(error.getMessage())
    }

    @SuppressLint("CheckResult")
    private fun updateUser() {
        val updateUser = UpdateData(
            profileEditName?.editText?.text.toString(),
            profileEditLastName?.editText?.text.toString(),
            profileEditPhone?.editText?.text.toString(),
            "https://lh6.googleusercontent.com/-wjvGYoAmvno/AAAAAAAAAAI/AAAAAAAAAAA/AMZuuckZi-nwUmiotqSHwlrNqzxT6ZbygQ/s96-c/photo.jpg"
        )

        viewModel.updateUserData(updateUser)
        bindUIData()
    }

    private fun updateUserAction(){
        when (profileSaveButton.text) {
            "Edytuj" -> {
                isActive(true)
                profileSaveButton.text = "Zapisz"
                profileCancelButton.visibility = View.VISIBLE
            }
            "Zapisz" -> {
                updateUser()
                isActive(false)
                profileSaveButton.text = "Edytuj"
                profileCancelButton.visibility = View.GONE
            }
        }
    }

    private fun cancelUpdateUser(){
        isActive(false)
        profileSaveButton.text = "Edytuj"
        profileCancelButton.visibility = View.GONE
        bindUIData()
    }

    private fun isActive(status: Boolean){
        profileEditNameText.isEnabled = status
        profileEditLastNameText.isEnabled = status
        profileEditPhoneText.isEnabled = status
    }

    override fun onPause() {
        disposable?.dispose()
        super.onPause()
    }
}
