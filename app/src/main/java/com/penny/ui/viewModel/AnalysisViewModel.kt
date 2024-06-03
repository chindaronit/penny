package com.penny.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penny.data.model.AnalysisItem
import com.penny.data.repository.AnalysisRepository
import com.penny.env.Status
import com.penny.ui.events.AnalysisScreenEvents
import com.penny.ui.sideEffects.ScreenSideEffects
import com.penny.ui.state.AnalysisState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository
): ViewModel() {
    private val _state: MutableStateFlow<AnalysisState> =
        MutableStateFlow(AnalysisState())
    val state: StateFlow<AnalysisState> = _state.asStateFlow()

    private val _effect: Channel<ScreenSideEffects> = Channel()

    fun sendEvent(event: AnalysisScreenEvents) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> ScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: AnalysisState) {
        _state.value = newState
    }

    private fun reduce(oldState: AnalysisState, event: AnalysisScreenEvents) {
        when(event){
            is AnalysisScreenEvents.AddOrUpdateAnalysisItem -> {
                addOrUpdateAnalysisItem(oldState,event.uid,event.analysisItem)
            }
            is AnalysisScreenEvents.GetAnalysisItems -> {
                getAnalysisItems(oldState, event.uid)
            }
        }
    }

    private fun getAnalysisItems(oldState: AnalysisState, uid: String) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {

            when (val result = analysisRepository.getAnalysisItems(uid)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your featured"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    setState(oldState.copy(isLoading = false, analysisItems = result.data))
                }
            }
        }
    }


    private fun addOrUpdateAnalysisItem(oldState: AnalysisState, uid: String, analysisItem: AnalysisItem) {
        viewModelScope.launch {
            when (val result = analysisRepository.addOrUpdateAnalysisItem(uid,analysisItem)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred during updating your featured"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {

                }
            }
        }
    }

}
