package com.example.tablicakorkowa.helpers

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}