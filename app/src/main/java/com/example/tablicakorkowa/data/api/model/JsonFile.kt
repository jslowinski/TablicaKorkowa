package com.example.tablicakorkowa.data.api.model

import com.google.gson.annotations.SerializedName

data class JsonFile(
    @SerializedName("NAZWA")
    val name: String
)