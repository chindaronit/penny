package com.penny.ui.screen


open class Screen(val route: String) {
    object MainScreen: Screen("main")
    object AnalysisScreen: Screen("analysis")
    object AccountsScreen: Screen("accounts")
    object AddScreen: Screen("add")
    object MoreScreen: Screen("more")
    object BiometricScreen: Screen("biometric")
    object LoginScreen: Screen("login")
    object Auth: Screen("auth")
    object App: Screen("app")
    object TransactionHistory: Screen("transactionHistory")
    object TransactionDetails: Screen("transactionDetails")


    fun withArgs(vararg args: String):String {
        return buildString {
            append(route)
            args.forEach {args->
                append("/$args")
            }
        }
    }
}