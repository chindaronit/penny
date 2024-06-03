package com.penny.ui.viewModel

import com.penny.data.repository.FeaturedRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penny.data.model.Featured
import com.penny.env.Status
import com.penny.ui.events.FeaturedScreenEvents
import com.penny.ui.sideEffects.ScreenSideEffects
import com.penny.ui.state.FeaturedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val featuredRepository: FeaturedRepository
): ViewModel() {
    private val _state: MutableStateFlow<FeaturedState> =
        MutableStateFlow(FeaturedState())
    val state: StateFlow<FeaturedState> = _state.asStateFlow()

    private val _effect: Channel<ScreenSideEffects> = Channel()

    fun sendEvent(event: FeaturedScreenEvents) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> ScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: FeaturedState) {
        _state.value = newState
    }

    private fun reduce(oldState: FeaturedState, event: FeaturedScreenEvents) {
        when(event){
            is FeaturedScreenEvents.GetFeatured -> {
                getFeatured(oldState,event.uid)
            }
            is FeaturedScreenEvents.UpdateFeatured -> {
                updateFeatured(oldState,event.uid,event.featured)
            }
            is FeaturedScreenEvents.ReloadFeatured -> {
                reloadFeatured(oldState,event.uid)
            }

            is FeaturedScreenEvents.WithoutReloadUpdateFeatured -> {
                withoutReloadUpdateFeatured(oldState,event.uid,event.featured)
            }
        }
    }

    private fun withoutReloadUpdateFeatured(oldState: FeaturedState, uid: String, featured: Featured) {
        viewModelScope.launch {
            when (val result = featuredRepository.updateFeatured(uid,featured)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred during updating your featured"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    println(errorMessage)
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {

                }
            }
        }
    }

    private fun getFeatured(oldState: FeaturedState, uid: String) {
        setState(oldState.copy(isLoading = true))
        viewModelScope.launch {

            when (val result = featuredRepository.getFeatured(uid)) {
                is Status.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your featured"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    setState(oldState.copy(isLoading = false, featured = result.data))
                }
            }
        }
    }

    private fun reloadFeatured(oldState: FeaturedState, uid: String) {
        viewModelScope.launch {
            when (val result = featuredRepository.getFeatured(uid)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your featured"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }

                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    setState(oldState.copy(featured = result.data))
                }
            }
        }
    }

    private fun updateFeatured(oldState: FeaturedState, uid: String,featured: Featured) {
        viewModelScope.launch {
            when (val result = featuredRepository.updateFeatured(uid,featured)) {
                is Status.Failure -> {
                    val errorMessage =
                        result.exception.message ?: "An error occurred during updating your featured"
                    setEffect { ScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                    println(errorMessage)
                    setState(oldState.copy(error = errorMessage))
                }

                is Status.Success -> {
                    sendEvent(event = FeaturedScreenEvents.ReloadFeatured(uid))
                }
            }
        }
    }

}
