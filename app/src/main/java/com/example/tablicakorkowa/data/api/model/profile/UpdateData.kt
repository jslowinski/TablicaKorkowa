package com.example.tablicakorkowa.data.api.model.profile

import com.google.gson.annotations.SerializedName

data class UpdateData(
    @SerializedName("firstname")
    var firstname: String,
    @SerializedName("lastname")
    var lastname: String,
    @SerializedName("telephone")
    var telephone: String,
    @SerializedName("avatar")
    var avatar: String

)