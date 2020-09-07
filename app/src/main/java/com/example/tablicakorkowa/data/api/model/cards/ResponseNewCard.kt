package com.example.tablicakorkowa.data.api.model.cards

import com.google.gson.annotations.SerializedName

data class ResponseNewCard(
    @SerializedName("id")
    var id: String
)