package com.example.tablicakorkowa.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.cards.CardsListAdapterData
import com.example.tablicakorkowa.data.api.model.levels.LevelDto
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.data.api.model.subjects.SubjectsDto
import com.example.tablicakorkowa.helpers.ErrorMessage
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.showErrorMessages
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.lang.Exception

interface DetailViewModelInterface {
    val cards: LiveData<CardsDto>
    val user: LiveData<UserDto>
    val subject: LiveData<SubjectsDto>
    val level: LiveData<LevelDto>
    val error: LiveData<ErrorMessage>
    fun getCard(id: String)
}

class DetailViewModel : ViewModel(), DetailViewModelInterface {

//    Public

    override val cards: LiveData<CardsDto>
        get() = cardsData

    override val subject: LiveData<SubjectsDto>
        get() = subjectsData

    override val user: LiveData<UserDto>
        get() = userData

    override val error: LiveData<ErrorMessage>
        get() = errorData

    override val level: LiveData<LevelDto>
        get() = levelData

//    Private

    private var cardsData = MutableLiveData<CardsDto>()
    private var userData = MutableLiveData<UserDto>()
    private var subjectsData = MutableLiveData<SubjectsDto>()
    private var levelData = MutableLiveData<LevelDto>()
    private var errorData = MutableLiveData<ErrorMessage>()

    private val apiService by lazy {
        ApiClient.create()
    }

    private var disposable: Disposable? = null
    private var disposable2: Disposable? = null
    private var disposable3: Disposable? = null
    private var disposable4: Disposable? = null

//    Methods

    override fun getCard(id: String) {
        Timber.e("Wywołuje się")
        Timber.e(id)
        disposable?.dispose()

        disposable = apiService.getSingleCard(id)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    Timber.e(result.toString())
                    cardsData.value = result[0]
                    getUser(result[0].userId)
                    getSubject(result[0].subjectID)
                    getLevel(result[0].levelId)
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    private fun getSubject(id: String) {
        disposable2?.dispose()

        disposable2 = apiService.getSubject(id)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    subjectsData.value = result[0]
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    private fun getUser(id: String) {
        disposable3?.dispose()

        disposable3 = apiService.userProfile(id)
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

    private fun getLevel(id: String) {
        disposable4?.dispose()

        disposable4 = apiService.getLevel(id)
            .subscribeOnIOThread()
            .observeOnMainThread()
            .showErrorMessages(errorData)
            .subscribe(
                {result ->
                    levelData.value = result[0]
                },
                {error ->
                    Timber.e(error)
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
        disposable2?.dispose()
        disposable3?.dispose()
        disposable4?.dispose()
        super.onCleared()
    }


}