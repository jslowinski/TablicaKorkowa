package com.example.tablicakorkowa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.cards.CardsListAdapterData
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.helpers.ErrorMessage
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.showErrorMessages
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import io.reactivex.disposables.Disposable
import timber.log.Timber

interface DashboardViewModelInterface {
    val cards: LiveData<List<CardsDto>>
    val error: LiveData<ErrorMessage>
    val cardsList: LiveData<List<CardsListAdapterData>>
    val user: LiveData<String>
    fun getAllCards()
}

class DashboardViewModel : ViewModel(), DashboardViewModelInterface{

//    Public

    override val cards: LiveData<List<CardsDto>>
        get() = cardsData

    override val user: LiveData<String>
        get() = userData

    override val error: LiveData<ErrorMessage>
        get() = errorData

    override val cardsList: LiveData<List<CardsListAdapterData>>
        get() = cardsListData

//    Private
    private val apiService by lazy {
        ApiClient.create()
}

    private var cardsData =  MutableLiveData<List<CardsDto>>()
    private val errorData = MutableLiveData<ErrorMessage>()
    private val userData = MutableLiveData<String>()
    private val cardsListData = MutableLiveData<List<CardsListAdapterData>>()

    private var disposable: Disposable? = null

//    Methods

    override fun getAllCards() {
        disposable?.dispose()

        disposable = apiService.getAllCards()
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    cardsData.value = result
                    for (i in result.indices){
                        apiService.userProfile(result[i].userId)
                            .subscribeOnIOThread()
                            .observeOnMainThread()
                            .showErrorMessages(errorData)
                            .subscribe(
                                {r ->
                                    cardsData.value!![i].userAvatar = r[0].avatar
                                },
                                {
                                    e -> Timber.e(e)
                                }
                            )
                    }
                },
                {error -> Timber.e(error)}
            )
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}