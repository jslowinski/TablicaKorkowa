package com.example.tablicakorkowa.data.api

import com.example.tablicakorkowa.data.api.Users.RequestRegister
import com.example.tablicakorkowa.data.api.Users.ResponseUser
import com.example.tablicakorkowa.data.api.Users.UsersDto
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //region Users

    @GET("users")
    fun fetchUsers(): Single<List<UsersDto>>

    @POST("users")
    fun register(@Body body: RequestRegister): Observable<ResponseUser>

    //endregion
}