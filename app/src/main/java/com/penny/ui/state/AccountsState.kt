package com.penny.ui.state

import com.penny.data.model.Accounts

data class AccountsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val accounts: List<Accounts> = emptyList(),
    val totalBalance: Double=0.0,
)
