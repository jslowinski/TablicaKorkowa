package com.example.tablicakorkowa.data.api.model

data class RequestRegister (
    var firstname: String,
    var lastname: String,
    var telephone: String,
    var email: String,
    var accountID: String,
    var avatar: String,
    var provider: String
)

