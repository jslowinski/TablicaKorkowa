package com.example.tablicakorkowa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.RequestRegister
import com.example.tablicakorkowa.data.api.model.ResponseUser
import com.example.tablicakorkowa.helpers.ErrorMessage
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.showErrorMessages
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import io.reactivex.disposables.Disposable
import timber.log.Timber

interface RegisterViewModelInterface {
    val user: LiveData<ResponseUser>
    val errors: LiveData<ErrorMessage>

    fun registerUser(requestRegister: RequestRegister)
}


class Register : ViewModel(), RegisterViewModelInterface {

    // Public Properties

    override val user: LiveData<ResponseUser>
        get() = userData

    override val errors: LiveData<ErrorMessage>
        get() = errorsData

    // Private Properties
    private val apiService by lazy {
        ApiClient.create()
    }

    private val userData = MutableLiveData<ResponseUser>()
    private val errorsData = MutableLiveData<ErrorMessage>()

    private var disposable: Disposable? = null



    //Internal Methods

    override fun registerUser(requestRegister: RequestRegister) {

        disposable?.dispose()

        disposable = apiService.register(requestRegister)
                .subscribeOnIOThread()
                .observeOnMainThread()
                .showErrorMessages(errorsData)
                .subscribe(
                    { result ->
                        userData.value = result
                        },
                    {error -> Timber.e(error)})

    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}