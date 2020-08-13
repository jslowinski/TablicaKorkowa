package com.example.tablicakorkowa.data.api.model.profile

import com.google.gson.annotations.SerializedName

data class ResponseUpdate(
    @SerializedName("message")
    var message: String
)