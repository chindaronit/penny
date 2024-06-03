package com.penny.ui.events

import com.penny.data.model.Transaction

sealed class TransactionScreenEvents {
    data class LoadAllTransactions(val uid: String) : TransactionScreenEvents()
    data class ReloadAllTransactions(val uid: String): TransactionScreenEvents()
    data class AddTransaction(val uid: String, val transaction: Transaction) : TransactionScreenEvents()
    data class GetTransaction(val id: String): TransactionScreenEvents()
}

