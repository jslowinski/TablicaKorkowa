package com.example.tablicakorkowa.data.api

import com.example.tablicakorkowa.data.api.model.RequestRegister
import com.example.tablicakorkowa.data.api.model.ResponseUser
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.profile.ResponseUpdate
import com.example.tablicakorkowa.data.api.model.profile.UpdateData
import com.example.tablicakorkowa.data.api.model.profile.UserDto
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

//    Subjects
//    @GET("subject")
}