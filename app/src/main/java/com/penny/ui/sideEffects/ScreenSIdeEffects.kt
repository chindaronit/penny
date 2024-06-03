package com.penny.ui.sideEffects

sealed class ScreenSideEffects {
    data class ShowSnackBarMessage(val message: String) : ScreenSideEffects()
}