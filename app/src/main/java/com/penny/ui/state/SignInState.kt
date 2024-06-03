package com.penny.ui.state

import com.penny.data.model.UserData

data class SignInState(
    val user: UserData? = null,
    val isSignInSuccessful: Boolean=false,
    val signInError: String?=null
)
