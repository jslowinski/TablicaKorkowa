package com.example.tablicakorkowa.data.api.model.cards

import com.google.gson.annotations.SerializedName

data class UpdateCardData(
    @SerializedName("description")
    var description: String,
    @SerializedName("isAbleToDrive")
    var isAbleToDrive: Boolean,
    @SerializedName("isOnline")
    var isOnline: Boolean,
    @SerializedName("price")
    var price: Float,
    @SerializedName("range")
    var range: Long,
    @SerializedName("status")
    var status: Long,
    @SerializedName("subjectID")
    var subjectID: String,
    @SerializedName("tmp_levelID")
    var levelID: String
)