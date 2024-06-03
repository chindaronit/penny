package com.penny.data.model

import com.google.firebase.Timestamp

data class Transaction(
    val id: String="",
    val uid: String="",
    val title: String = "",
    val categoryInd: Long=0,
    val timestamp: Timestamp = Timestamp.now(),
    val amount: String = "",
    val paymentSourceInd: Long=0,
    val description: String = "",
)
