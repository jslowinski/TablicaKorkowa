package com.example.tablicakorkowa.data.api.model.levels

import com.google.gson.annotations.SerializedName

data class LevelDto(
    @SerializedName("id")
    var id: String,
    @SerializedName("value")
    var value: String
)
