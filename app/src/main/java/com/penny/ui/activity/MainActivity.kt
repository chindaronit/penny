package com.penny.ui.activity

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.Identity
import com.penny.data.model.UserData
import com.penny.env.GoogleAuthUIClient
import com.penny.env.SIDE_EFFECTS_KEY
import com.penny.ui.biometric.BiometricPromptManager
import com.penny.ui.screen.AccountsScreen
import com.penny.ui.screen.AddScreen
import com.penny.ui.screen.AnalysisScreen
import com.penny.ui.screen.BiometricScreen
import com.penny.ui.screen.LoginScreen
import com.penny.ui.screen.MainScreen
import com.penny.ui.screen.MoreScreen
import com.penny.ui.screen.Screen
import com.penny.ui.screen.TransactionDetails
import com.penny.ui.screen.TransactionHistory
import com.penny.ui.sideEffects.ScreenSideEffects
import com.penny.ui.theme.PennyTheme
import com.penny.ui.viewModel.AccountsViewModel
import com.penny.ui.viewModel.AnalysisViewModel
import com.penny.ui.viewModel.FeaturedViewModel
import com.penny.ui.viewModel.SignInViewModel
import com.penny.ui.viewModel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val biometricManager by lazy {
        BiometricPromptManager(this)
    }

    // Lazy initialization of GoogleAuthUIClient
    private val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { screen ->
            val zoomX = ObjectAnimator.ofFloat(
                screen.iconView,
                View.SCALE_X,
                0.4f,
                0.0f
            ).apply {
                interpolator = OvershootInterpolator()
                duration = 500L
                doOnEnd { screen.remove() }
            }

            val zoomY = ObjectAnimator.ofFloat(
                screen.iconView,
                View.SCALE_Y,
                0.4f,
                0.0f
            ).apply {
                interpolator = OvershootInterpolator()
                duration = 500L
                doOnEnd { screen.remove() }
            }

            zoomX.start()
            zoomY.start()
        }

        enableEdgeToEdge()
        setContent {
            PennyTheme {
                val navController = rememberNavController()
                val accountsViewModel: AccountsViewModel = viewModel()
                val accountState = accountsViewModel.state.collectAsState().value
                val transactionViewModel: TransactionViewModel = viewModel()
                val transactionState = transactionViewModel.state.collectAsState().value
                val featuredViewModel: FeaturedViewModel = viewModel()
                val featuredState = featuredViewModel.state.collectAsState().value
                val analysisViewModel: AnalysisViewModel = viewModel()
                val analysisState = analysisViewModel.state.collectAsState().value
                val snackBarHostState = remember { SnackbarHostState() }
                val effectFlow = transactionViewModel.effect
                val accountEffectFlow = accountsViewModel.effect
                val authViewModel: SignInViewModel = viewModel()
                val authState = authViewModel.state.collectAsState().value

                var user by remember { mutableStateOf<UserData?>(null) }
                var authenticated by rememberSaveable {
                    mutableStateOf(false)
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                LaunchedEffect(key1 = Unit) {
                    user = googleAuthUIClient.getSignedInUser()
                    if (user != null) {
                        navController.navigate(Screen.BiometricScreen.route) {
                            popUpTo(Screen.LoginScreen.route)
                        }
                    }
                }


//     Handle successful sign-in
                LaunchedEffect(key1 = authState.isSignInSuccessful) {
                    if (authState.isSignInSuccessful) {
                        user = authState.user
                        navController.navigate(Screen.BiometricScreen.route) {
                            popUpTo(Screen.LoginScreen.route)
                        }
                        authViewModel.resetState()
                    }
                }

                LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
                    effectFlow.onEach { effect ->
                        when (effect) {
                            is ScreenSideEffects.ShowSnackBarMessage -> {
                                snackBarHostState.showSnackbar(
                                    message = effect.message,
                                    duration = SnackbarDuration.Short,
                                    actionLabel = "DISMISS",
                                )
                            }
                        }
                    }.launchIn(this)
                    accountEffectFlow.onEach { effect ->
                        when (effect) {
                            is ScreenSideEffects.ShowSnackBarMessage -> {
                                snackBarHostState.showSnackbar(
                                    message = effect.message,
                                    duration = SnackbarDuration.Short,
                                    actionLabel = "DISMISS",
                                )
                            }
                        }
                    }.launchIn(this)
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Auth.route,
                    enterTransition = {
                        EnterTransition.None
                    },
                    exitTransition = {
                        ExitTransition.None
                    }

                ) {
                    navigation(
                        startDestination = Screen.LoginScreen.route,
                        route = Screen.Auth.route
                    ) {
                        composable(Screen.BiometricScreen.route) {
                            if (authenticated) {
                                navController.navigate(Screen.App.route) {
                                    popUpTo(Screen.Auth.route)
                                }
                            } else {
                                user?.let { it1 ->
                                    BiometricScreen(
                                        user = it1,
                                        biometricManager = biometricManager,
                                        navController = navController,
                                        snackBarHostState = snackBarHostState
                                    ) {
                                        authenticated = it
                                    }
                                }
                            }

                        }
                        composable(Screen.LoginScreen.route) {

                            // Initialize launcher for activity result
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleOwner.lifecycleScope.launch {
                                            val signInResult = googleAuthUIClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            authViewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LoginScreen(
                                state = authState,
                                onSignInClick = {
                                    lifecycleOwner.lifecycleScope.launch {
                                        // Initiate sign-in process
                                        val signInIntentSender = googleAuthUIClient.signIn()
                                        // Launch the sign-in intent sender
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                    }

                    navigation(
                        startDestination = Screen.MainScreen.route,
                        route = Screen.App.route
                    ) {
                        composable(Screen.MainScreen.route) {
                            user?.let { it1 ->
                                MainScreen(
                                    user = it1,
                                    snackBarHostState = snackBarHostState,
                                    navController,
                                    transactionState = transactionState,
                                    featuredState = featuredState,
                                    transactionViewModel = transactionViewModel,
                                    featuredViewModel = featuredViewModel,
                                    accountsState = accountState,
                                    accountsViewModel = accountsViewModel
                                )
                            }
                        }
                        composable(Screen.AccountsScreen.route) {
                            user?.let { it1 ->
                                AccountsScreen(
                                    uid = it1.userId!!,
                                    snackBarHostState,
                                    accountsViewModel,
                                    accountState,
                                    navController,
                                    featuredState = featuredState,
                                    featuredViewModel = featuredViewModel
                                )
                            }
                        }
                        composable(Screen.AnalysisScreen.route) {
                            user?.let { it1 ->
                                AnalysisScreen(
                                    uid = it1.userId!!,
                                    navController,
                                    analysisState,
                                    analysisViewModel
                                )
                            }
                        }
                        composable(Screen.AddScreen.route) {
                            user?.let { it1 ->
                                AddScreen(
                                    uid = it1.userId!!,
                                    snackBarHostState = snackBarHostState,
                                    navController = navController,
                                    viewModel = transactionViewModel,
                                    analysisViewModel = analysisViewModel,
                                    featuredState = featuredState,
                                    featuredViewModel = featuredViewModel,
                                    accountsState = accountState,
                                    accountsViewModel = accountsViewModel
                                )
                            }
                        }
                        composable(Screen.MoreScreen.route) {
                            user?.let { it1 ->
                                MoreScreen(
                                    user = it1,
                                    navController = navController,
                                    featuredState = featuredState,
                                    featuredViewModel = featuredViewModel
                                ) {
                                    lifecycleOwner.lifecycleScope.launch {
                                        // Sign out the user
                                        googleAuthUIClient.signOut()
                                        // Navigate back to sign-in screen
                                        navController.navigate(Screen.Auth.route) {
                                            popUpTo(Screen.App.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    composable(Screen.TransactionHistory.route) {
                        user?.let { it1 ->
                            TransactionHistory(
                                uid = it1.userId!!,
                                navController,
                                transactionState,
                                transactionViewModel
                            )
                        }
                    }
                    composable(
                        route = Screen.TransactionDetails.route + "/{transactionId}",
                        arguments = listOf(
                            navArgument("transactionId") {
                                type = NavType.StringType
                                defaultValue = ""
                                nullable = false
                            }
                        )
                    ) { entry ->
                        TransactionDetails(
                            navController,
                            transactionState,
                            transactionViewModel,
                            transactionId = entry.arguments?.getString("transactionId") ?: "",
                        )
                    }

                }


            }
        }
    }
}
