package com.penny.ui.viewModel

import androidx.lifecycle.ViewModel
import com.penny.data.model.SignInResult
import com.penny.env.GoogleAuthUIClient
import com.penny.ui.state.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ViewModel class responsible for managing the state related to sign-in process
class SignInViewModel : ViewModel() {
    // MutableStateFlow to hold the current state of the sign-in process
    private val _state = MutableStateFlow(SignInState())

    // Exposed immutable StateFlow to observe the sign-in state changes
    val state = _state.asStateFlow()

    // Function to handle the result of the sign-in attempt
    fun onSignInResult(result: SignInResult) {
        // Update the state based on the sign-in result
        _state.update { currentState ->
            // Copy the current state with updated sign-in success flag and error message
            currentState.copy(
                user = result.data,
                isSignInSuccessful = result.data != null, // Update sign-in success flag
                signInError = result.errorMessage // Update error message
            )
        }
    }

    fun loadUser(googleAuthUIClient: GoogleAuthUIClient) {
        val userData = googleAuthUIClient.getSignedInUser()
        _state.update { currentState ->
            // Copy the current state with updated sign-in success flag and error message
            currentState.copy(
                user = userData
            )
        }
    }

    fun signOut(){
        _state.update {
            it.copy(
                user=null
            )
        }
    }


    // Function to reset the state to its initial values
    fun resetState() {
        // Update the state to a new instance of SignInState with default values
        _state.update { SignInState() }
    }
}
