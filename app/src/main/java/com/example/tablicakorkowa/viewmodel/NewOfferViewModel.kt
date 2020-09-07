package com.example.tablicakorkowa.viewmodel

import android.widget.Toast
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.tablicakorkowa.HomeFragmentDirections
import com.example.tablicakorkowa.NewOfferFragment
import com.example.tablicakorkowa.NewOfferFragmentDirections
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.cards.NewCardData
import com.example.tablicakorkowa.data.api.model.cards.UpdateCardData
import com.example.tablicakorkowa.data.api.model.levels.LevelDto
import com.example.tablicakorkowa.data.api.model.subjects.SubjectsDto
import com.example.tablicakorkowa.helpers.ErrorMessage
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.showErrorMessages
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

interface NewOfferViewModelInterface{
    val card: LiveData<CardsDto>
    val subject: LiveData<List<SubjectsDto>>
    val level: LiveData<List<LevelDto>>
    val error: LiveData<ErrorMessage>
    fun getSubjects()
    fun getLevels()
    fun createNewOffer(card: NewCardData, fragment: NewOfferFragment)
    fun getOfferInfo(id: String)
    fun updateOffer(id: String, card: UpdateCardData, fragment: NewOfferFragment)
}

class NewOfferViewModel : ViewModel(), NewOfferViewModelInterface{

//    Public

    override val level: LiveData<List<LevelDto>>
        get() = levelData

    override val subject: LiveData<List<SubjectsDto>>
        get() = subjectData

    override val error: LiveData<ErrorMessage>
        get() = errorData

    override val card: LiveData<CardsDto>
        get() = cardData

//    Private

    private val apiService by lazy{
        ApiClient.create()
    }

    private var subjectData = MutableLiveData<List<SubjectsDto>>()
    private var levelData = MutableLiveData<List<LevelDto>>()
    private var errorData = MutableLiveData<ErrorMessage>()
    private var cardData = MutableLiveData<CardsDto>()

    private var disposable: Disposable? = null
    private var disposable2: Disposable? = null
    private var disposable3: Disposable? = null
    private var disposable4: Disposable? = null
    private var disposable5: Disposable? = null
    private var compositeDisposable: CompositeDisposable? = null

//    Methods

    override fun getSubjects() {
        disposable?.dispose()

        disposable = apiService.getSubjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result ->
                    subjectData.value = result
                },
                {error ->
                    Timber.e(error)
                }
            )

        compositeDisposable?.add(disposable!!)
    }

    override fun getLevels() {
        disposable2?.dispose()

        disposable2 = apiService.getLevels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result ->
                    levelData.value = result
                },
                {error ->
                    Timber.e(error)
                }
            )

        compositeDisposable?.add(disposable2!!)
    }

    override fun createNewOffer(card: NewCardData, fragment: NewOfferFragment) {
        disposable3?.dispose()
        compositeDisposable?.add(disposable3!!)

        disposable3 = apiService.createNewCard(card)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    Timber.e(result.id)
                    fragment.findNavController().navigate(NewOfferFragmentDirections.actionNewOfferFragmentToHome())
                    val toast = Toast.makeText(fragment.context, "Oferta pomyślne utworzona", Toast.LENGTH_SHORT)
                    toast.show()
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    override fun getOfferInfo(id: String) {
        disposable4?.dispose()
        compositeDisposable?.add(disposable4!!)

        disposable4 = apiService.getSingleCard(id)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    cardData.value = result[0]
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    override fun updateOffer(id: String, card: UpdateCardData, fragment: NewOfferFragment) {
        disposable5?.dispose()
        compositeDisposable?.add(disposable5!!)

        disposable5 = apiService.updateCard(id, card)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    Timber.e(result.message)
                    fragment.findNavController().navigate(NewOfferFragmentDirections.actionNewOfferFragmentToHome())
                    val toast = Toast.makeText(fragment.context, "Oferta pomyślne zaaktualizowana", Toast.LENGTH_SHORT)
                    toast.show()
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    override fun onCleared() {
        compositeDisposable?.dispose()
        super.onCleared()
    }
}