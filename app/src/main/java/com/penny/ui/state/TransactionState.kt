package com.penny.ui.state

import com.penny.data.model.Transaction

data class TransactionState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val transactions: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction?= null
)