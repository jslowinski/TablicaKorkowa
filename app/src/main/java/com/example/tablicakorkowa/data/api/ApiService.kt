package com.example.tablicakorkowa.data.api

import com.example.tablicakorkowa.data.api.model.RequestRegister
import com.example.tablicakorkowa.data.api.model.ResponseUser
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.cards.NewCardData
import com.example.tablicakorkowa.data.api.model.cards.ResponseNewCard
import com.example.tablicakorkowa.data.api.model.cards.UpdateCardData
import com.example.tablicakorkowa.data.api.model.levels.LevelDto
import com.example.tablicakorkowa.data.api.model.profile.ResponseUpdate
import com.example.tablicakorkowa.data.api.model.profile.UpdateData
import com.example.tablicakorkowa.data.api.model.profile.UserDto
import com.example.tablicakorkowa.data.api.model.subjects.SubjectsDto
import io.reactivex.Observable
import retrofit2.http.*


interface ApiService {
//    Users

    @POST("users")
    fun register(@Body body: RequestRegister): Observable<ResponseUser>

    @GET("users/accountID/{id}")
    fun userProfile(@Path("id") id: String?): Observable<List<UserDto>>

    @PUT("users/{id}")
    fun updateUserProfile(@Path("id") id: String?, @Body body: UpdateData): Observable<ResponseUpdate>

//    Cards
    @GET("cards")
    fun getAllCards(): Observable<List<CardsDto>>

    @GET("cards/{id}")
    fun getSingleCard(@Path("id") id: String?): Observable<List<CardsDto>>

    @GET("cards/UserID/{id}")
    fun getUserCard(@Path("id") id: String?): Observable<List<CardsDto>>

    @POST("cards")
    fun createNewCard(@Body body: NewCardData): Observable<ResponseNewCard>

    @PUT("cards/{id}")
    fun updateCard(@Path("id") id: String?, @Body body: UpdateCardData): Observable<ResponseUpdate>

    @DELETE("cards/{id}")
    fun deleteCard(@Path("id") id: String?): Observable<ResponseUpdate>

//    Subjects
    @GET("subjects")
    fun getSubjects(): Observable<List<SubjectsDto>>

    @GET("subjects/{id}")
    fun getSubject(@Path("id") id: String?): Observable<List<SubjectsDto>>

//    Levels
    @GET("levels")
    fun getLevels(): Observable<List<LevelDto>>

    @GET("levels/{id}")
    fun getLevel(@Path("id") id: String?): Observable<List<LevelDto>>
}