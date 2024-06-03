package com.penny.ui.navigation

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
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
import com.penny.ui.viewModel.AccountsViewModel
import com.penny.ui.viewModel.AnalysisViewModel
import com.penny.ui.viewModel.FeaturedViewModel
import com.penny.ui.viewModel.SignInViewModel
import com.penny.ui.viewModel.TransactionViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
fun BottomNavGraph(
    navController: NavHostController,
    biometricManager: BiometricPromptManager,
    googleAuthUIClient: GoogleAuthUIClient
) {


}