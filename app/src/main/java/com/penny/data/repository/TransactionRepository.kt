package com.penny.data.repository

import com.penny.data.model.Transaction
import com.penny.env.Status

interface TransactionRepository{
    suspend fun getTransaction(id: String): Status<Transaction>
    suspend fun getAllTransactions(uid: String): Status<List<Transaction>>
    suspend fun addTransaction(uid: String, transaction: Transaction): Status<Unit>
    suspend fun deleteTransaction(id: String): Status<Unit>

}