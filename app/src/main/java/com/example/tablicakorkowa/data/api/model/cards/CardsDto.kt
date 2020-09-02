package com.example.tablicakorkowa.data.api.model.cards

import com.google.gson.annotations.SerializedName

data class CardsDto(
    @SerializedName("id")
    var id: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("created")
    var created: Long,
    @SerializedName("deleted")
    var deleted: Long,
    @SerializedName("description")
    var description: String,
    @SerializedName("endDate")
    var endDate: Long,
    @SerializedName("isAbleToDrive")
    var isAbleToDrive: Boolean,
    @SerializedName("isHit")
    var isHit: Boolean,
    @SerializedName("isOnline")
    var isOnline: Boolean,
    @SerializedName("levelId")
    var levelId: String,
    @SerializedName("price")
    var price: Double,
    @SerializedName("range")
    var range: Int,
    @SerializedName("startDate")
    var startDate: Long,
    @SerializedName("status")
    var status: Int,
    @SerializedName("subjectID")
    var subjectID: String,
    @SerializedName("tittle")
    var title: String,
    @SerializedName("type")
    var type: Int,
    @SerializedName("userId")
    var userId: String,
    @SerializedName("viewsId")
    var viewsId: String,
    @SerializedName("province")
    var province: String,


    //Data to list
    var userAvatar: String,
    var subjectName: String,
    var isOnlineString: String,
    var isDriveString: String
)