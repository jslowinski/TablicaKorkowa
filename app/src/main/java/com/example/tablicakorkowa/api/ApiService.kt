package com.example.tablicakorkowa.api

import com.example.tablicakorkowa.api.Users.RequestRegister
import com.example.tablicakorkowa.api.Users.ResponseUser
import com.example.tablicakorkowa.api.Users.UsersDto
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //region Users

    @GET("users")
    fun fetchUsers(): Single<List<UsersDto>>

    @POST("users")
    fun register(@Body body: RequestRegister): Single<ResponseUser>

    //endregion
}