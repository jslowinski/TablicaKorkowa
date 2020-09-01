package com.example.tablicakorkowa.data.api.model.subjects

import com.google.gson.annotations.SerializedName

data class SubjectsDto(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String
)