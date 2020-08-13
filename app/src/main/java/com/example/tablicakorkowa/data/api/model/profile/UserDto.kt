package com.example.tablicakorkowa.data.api.model.profile

import com.google.gson.annotations.SerializedName

data class UserDto (
    @SerializedName("id")
    var id: String,
    @SerializedName("firstName")
    var firstname: String,
    @SerializedName("lastName")
    var lastname: String,
    @SerializedName("telephone")
    var telephone: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("accountID")
    var accountID: String,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("provider")
    var provider: String
)