package com.example.tablicakorkowa.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.profile.UpdateData
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.helpers.*
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profil.*
import timber.log.Timber

interface ProfileViewModelInterface {
    val user: LiveData<UserDto>
    val error: LiveData<ErrorMessage>

    fun getUserProfile(id: String)
    fun updateUserData(user: UpdateData)
}

class ProfileViewModel : ViewModel(), ProfileViewModelInterface {

    //Public

    override val user: LiveData<UserDto>
        get() = userData

    override val error: LiveData<ErrorMessage>
        get() = errorData


    //Private
    private val apiService by lazy {
        ApiClient.create()
    }

    private val userData = MutableLiveData<UserDto>()
    private val errorData = MutableLiveData<ErrorMessage>()

    private var disposable: Disposable? = null


    //Methods

    override fun getUserProfile(id: String) {

        disposable?.dispose()

        disposable = apiService.userProfile(id)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    userData.value = result[0]
                },
                {error -> Timber.e(error)}
            )
    }


    @SuppressLint("CheckResult")
    override fun updateUserData(user: UpdateData) {

        apiService.updateUserProfile( userData.value!!.id, user)
            .subscribeOn(Schedulers.io())
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    Timber.e(result.message)
                    getUserProfile(getUid())
                },
                {error -> Timber.e(error)}
            )
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}
