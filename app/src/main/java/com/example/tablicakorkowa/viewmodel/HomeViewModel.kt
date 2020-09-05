package com.example.tablicakorkowa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.helpers.ErrorMessage
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.showErrorMessages
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import io.reactivex.disposables.Disposable
import timber.log.Timber

interface HomeViewModelInteface{
    val cards: LiveData<List<CardsDto>>
    val error: LiveData<ErrorMessage>
    fun getAllCards(userId: String)
}

class HomeViewModel : ViewModel(), HomeViewModelInteface{

//    Public

    override val cards: LiveData<List<CardsDto>>
        get() = cardsData

    override val error: LiveData<ErrorMessage>
        get() = errorData

//    Private

    private val apiService by lazy {
        ApiClient.create()
    }

    private var cardsData = MutableLiveData<List<CardsDto>>()
    private var errorData = MutableLiveData<ErrorMessage>()

    private var disposable: Disposable? = null

//    Methods

    override fun getAllCards(userId: String) {
        disposable?.dispose()

        disposable = apiService.getUserCard(userId)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    cardsData.value = result
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}