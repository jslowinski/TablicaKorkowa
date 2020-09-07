package com.example.tablicakorkowa.data.api.model.cards

import com.google.gson.annotations.SerializedName

data class NewCardData(
    @SerializedName("description")
    var description: String,
    @SerializedName("startDate")
    var startDate: Long,
    @SerializedName("endDate")
    var endDate: Long,
    @SerializedName("isHit")
    var isHit: Boolean,
    @SerializedName("userID")
    var userID: String,
    @SerializedName("type")
    var type: Int,
    @SerializedName("price")
    var price: Float,
    @SerializedName("isAbleToDrive")
    var isAbleToDrive: Boolean,
    @SerializedName("range")
    var range: Long,
    @SerializedName("city")
    var city: String,
    @SerializedName("province")
    var province: String,
    @SerializedName("isOnline")
    var isOnline: Boolean,
    @SerializedName("levelID")
    var levelID: String,
    @SerializedName("subjectID")
    var subjectID: String,
    @SerializedName("tittle")
    var tittle: String
)