package com.penny.data.repository

import com.penny.data.model.Accounts
import com.penny.env.Status

interface AccountsRepository {
    suspend fun getAllAccounts(uid: String): Status<List<Accounts>>
    suspend fun addAccount(uid: String,account: Accounts): Status<Unit>
    suspend fun updateAccount(id: String, account: Accounts): Status<Unit>
    suspend fun deleteAccount(id: String): Status<Unit>
    suspend fun reduceAccountBalance(uid:String,sourceInd: Long, amount: Double): Status<Unit>
}