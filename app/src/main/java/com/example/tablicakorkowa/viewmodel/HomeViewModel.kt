package com.example.tablicakorkowa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tablicakorkowa.adapters.HomeListAdapter
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.profile.ResponseUpdate
import com.example.tablicakorkowa.helpers.ErrorMessage
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.showErrorMessages
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.fastadapter.FastAdapter
import io.reactivex.disposables.Disposable
import timber.log.Timber

interface HomeViewModelInteface{
    val cards: LiveData<List<CardsDto>>
    val error: LiveData<ErrorMessage>
    val deleteResult: LiveData<ResponseUpdate>
    fun getAllCards(userId: String)
    fun deleteCard(id: String)
}

class HomeViewModel : ViewModel(), HomeViewModelInteface{

//    Public

    override val cards: LiveData<List<CardsDto>>
        get() = cardsData

    override val error: LiveData<ErrorMessage>
        get() = errorData

    override val deleteResult: LiveData<ResponseUpdate>
        get() = deleteResultData

//    Private

    private val apiService by lazy {
        ApiClient.create()
    }

    private var cardsData = MutableLiveData<List<CardsDto>>()
    private var errorData = MutableLiveData<ErrorMessage>()
    private var deleteResultData = MutableLiveData<ResponseUpdate>()

    private var disposable: Disposable? = null
    private var disposable2: Disposable? = null

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

    override fun deleteCard(id: String) {
        disposable2?.dispose()

        disposable2 = apiService.deleteCard(id)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    deleteResultData.postValue(result)
//                    getAllCards(FirebaseAuth.getInstance().currentUser!!.uid)
//                    fastAdapter.notifyAdapterDataSetChanged()
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