package com.penny.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penny.data.model.Transaction
import com.penny.data.repository.TransactionRepository
import com.penny.env.Status
import com.penny.ui.events.TransactionScreenEvents
import com.penny.ui.sideEffects.ScreenSideEffects
import com.penny.ui.state.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {
    private val _state: MutableStateFlow<TransactionState> =
        MutableStateFlow(TransactionState())
    val state: StateFlow<TransactionState> = _state.asStateFlow()

    private val _effect: Channel<ScreenSideEffects> = Channel()
    val effect= _effect.receiveAsFlow()
    fun sendEvent(event: TransactionScreenEvents) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> ScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: TransactionState) {
        _state.value = newState
    }

    private fun reduce(oldState: TransactionState, event: TransactionScreenEvents) {
        when(event){
            is TransactionScreenEvents.AddTransaction -> {
                addTransaction(oldState,event.uid,event.transaction)
            }
            is TransactionScreenEvents.LoadAllTransactions -> {
                loadAllTransactions(oldState,event.uid)
            }
            is TransactionScreenEvents.GetTransaction -> {
                getTransaction(oldState,event.id)
            }
            is TransactionScreenEvents.ReloadAllTransactions -> {
                reloadAllTransactions(oldState,event.uid)
            }
        }
    }

    private fun getTransaction(oldState: TransactionState, id: String) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {

            when (val result = transactionRepository.getTransaction(id)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your Accounts"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    setState(oldState.copy(isLoading = false, selectedTransaction = result.data))
                }
            }
        }
    }

    private fun reloadAllTransactions(oldState: TransactionState, uid: String) {
        viewModelScope.launch {
            when (val result = transactionRepository.getAllTransactions(uid)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your transactions"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    setState(oldState.copy(transactions = result.data))
                }
            }
        }
    }

    private fun addTransaction(oldState: TransactionState, uid: String, transaction: Transaction) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {
            when (val result = transactionRepository.addTransaction(uid = uid,transaction=transaction)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))
                    val errorMessage =
                        result.exception.message ?: "An error occurred in adding transaction"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Status.Success -> {
                    setState(oldState.copy(isLoading = false))
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = "Expense added successfully") }
                }
            }
        }
    }


    private fun loadAllTransactions(oldState: TransactionState, uid: String) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {

            when (val result = transactionRepository.getAllTransactions(uid)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your Accounts"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    setState(oldState.copy(isLoading = false, transactions = result.data))
                }
            }
        }
    }


}
