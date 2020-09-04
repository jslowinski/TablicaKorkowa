package com.example.tablicakorkowa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.helpers.*
import io.reactivex.disposables.Disposable
import timber.log.Timber

interface UserCardsViewModelInterface {
    val cards: LiveData<List<CardsDto>>
    val users: LiveData<UserDto>
    val error: LiveData<ErrorMessage>
    fun getAllInfo(userId: String)

}

class UserCardsViewModel : ViewModel(), UserCardsViewModelInterface{

//    Public

    override val cards: LiveData<List<CardsDto>>
        get() = cardsData

    override val users: LiveData<UserDto>
        get() = userData

    override val error: LiveData<ErrorMessage>
        get() = errorData

//    Private

    private val apiService by lazy {
        ApiClient.create()
    }

    private var cardsData = MutableLiveData<List<CardsDto>>()
    private var userData = MutableLiveData<UserDto>()
    private var errorData = MutableLiveData<ErrorMessage>()

    private var disposable: Disposable? = null

//    Methods

    override fun getAllInfo(userId: String) {
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

        disposable = apiService.userProfile(userId)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    userData.value = result[0]
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