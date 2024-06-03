package com.penny.ui.events

import com.penny.data.model.Accounts

sealed class AccountsScreenEvents {
    data class FirstLoadGetAllAccounts(val uid: String) : AccountsScreenEvents()
    data class ReloadGetAllAccounts(val uid: String) : AccountsScreenEvents()
    data class AddAccount(val uid: String, val account: Accounts) : AccountsScreenEvents()
    data class UpdateAccount(val id: String, val account: Accounts) : AccountsScreenEvents()
    data class ReduceAccountBalanceWithSourceInd(val uid: String,val sourceInd: Long, val amount: Double) : AccountsScreenEvents()
    data class DeleteAccount(val id: String,val uid: String) : AccountsScreenEvents()
}
