package com.penny.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penny.data.model.Accounts
import com.penny.data.repository.AccountsRepository
import com.penny.env.Status
import com.penny.ui.events.AccountsScreenEvents
import com.penny.ui.sideEffects.ScreenSideEffects
import com.penny.ui.state.AccountsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountsRepository: AccountsRepository
): ViewModel() {
    private val _state: MutableStateFlow<AccountsState> =
        MutableStateFlow(AccountsState())
    val state: StateFlow<AccountsState> = _state.asStateFlow()

    private val _effect: Channel<ScreenSideEffects> = Channel()
    val effect= _effect.receiveAsFlow()

    fun sendEvent(event: AccountsScreenEvents) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> ScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: AccountsState) {
        _state.value = newState
    }

    private fun reduce(oldState: AccountsState, event: AccountsScreenEvents) {
        when(event){
            is AccountsScreenEvents.AddAccount -> {
                addAccount(oldState,event.uid,event.account)
            }
            is AccountsScreenEvents.DeleteAccount -> {
                deleteAccount(oldState,event.id,event.uid)
            }
            is AccountsScreenEvents.FirstLoadGetAllAccounts ->{
                getAllAccounts(oldState,event.uid)
            }
            is AccountsScreenEvents.UpdateAccount -> {
                updateAccount(oldState,event.id, event.account)
            }
            is AccountsScreenEvents.ReloadGetAllAccounts -> {
                reloadGetAllAccounts(oldState,event.uid)
            }
            is AccountsScreenEvents.ReduceAccountBalanceWithSourceInd -> {
                reduceAccountBalanceWithSourceInd(oldState,event.uid,event.sourceInd,event.amount)
            }
        }
    }

    private fun reduceAccountBalanceWithSourceInd(oldState: AccountsState, uid: String ,sourceInd: Long, amount: Double) {
        viewModelScope.launch {
            when (val result = accountsRepository.reduceAccountBalance(uid,sourceInd,amount)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred"
                    setState(oldState.copy(error = errorMessage))
                }
                is Status.Success -> {
                }
            }
        }
    }

    private fun reloadGetAllAccounts(oldState: AccountsState, uid: String) {
        viewModelScope.launch {
            when (val result = accountsRepository.getAllAccounts(uid)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your Accounts"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    var balance=0.0
                    for(account in result.data){
                        balance += account.balance.toDouble()
                    }
                    setState(oldState.copy(totalBalance=balance,accounts = result.data))
                }
            }
        }
    }

    private fun addAccount(oldState: AccountsState, uid: String, account: Accounts) {

        viewModelScope.launch {
            when (val result = accountsRepository.addAccount(uid=uid, account = account)) {
                is Status.Failure -> {

                    val errorMessage =
                        result.exception.message ?: "An error occurred in adding Account"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Status.Success -> {
                    sendEvent(AccountsScreenEvents.ReloadGetAllAccounts(uid))
                }
            }
        }
    }

    private fun deleteAccount(oldState: AccountsState, id: String,uid: String) {
        viewModelScope.launch {
            when (val result = accountsRepository.deleteAccount(id)) {
                is Status.Failure -> {

                    val errorMessage =
                        result.exception.message ?: "An error occurred during deleting Account"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Status.Success -> {
                    sendEvent(AccountsScreenEvents.ReloadGetAllAccounts(uid))
                }
            }
        }
    }

    private fun updateAccount(oldState: AccountsState, id: String, account: Accounts) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {
            when (val result = accountsRepository.updateAccount(id, account)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when updating note"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Status.Success -> {
                    setState(oldState.copy(isLoading = false))
                    sendEvent(AccountsScreenEvents.ReloadGetAllAccounts(account.uid))
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = "Updated Successfully") }
                }
            }
        }
    }

    private fun getAllAccounts(oldState: AccountsState, uid: String) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {

            when (val result = accountsRepository.getAllAccounts(uid)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your Accounts"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    var balance=0.0
                    for(account in result.data){
                        balance += account.balance.toDouble()
                    }
                    setState(oldState.copy(isLoading = false, totalBalance = balance,accounts = result.data))
                }
            }
        }
    }


}
