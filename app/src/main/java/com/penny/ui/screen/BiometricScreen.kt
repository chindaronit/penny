package com.penny.ui.screen

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.penny.R
import com.penny.data.model.UserData
import com.penny.ui.biometric.BiometricPromptManager
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiometricScreen(
    user: UserData,
    biometricManager: BiometricPromptManager,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    onClick: (Boolean)->Unit
) {
    val biometricResult by biometricManager.promptResults.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Activity result: $it")
        }
    )
    var locked by remember {
        mutableStateOf(true)
    }
    // State to track if the biometric prompt has been shown
    var promptShown by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = biometricResult) {
        when (biometricResult) {
            is BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                enrollLauncher.launch(enrollIntent)
            }

            is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                locked = false
                onClick(true)
                navController.navigate(Screen.App.route){
                    popUpTo(Screen.Auth.route){
                        inclusive=true
                    }
                }
            }

            is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                scope.launch {
                    snackBarHostState.showSnackbar("Error Authenticating...")
                }
            }

            BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                scope.launch {
                    snackBarHostState.showSnackbar("Authentication Failed...")
                }
            }

            BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                scope.launch {
                    snackBarHostState.showSnackbar("Biometric Feature Unavailable")
                }
                navController.navigate(Screen.App.route){
                    popUpTo(Screen.Auth.route){
                        inclusive=true
                    }
                }
            }

            BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
                scope.launch {
                    snackBarHostState.showSnackbar("Biometric Hardware Unavailable")
                }
            }

            null -> {
                // Do nothing or show a default message
                // You can add a default case if needed
            }
        }
    }


    // Show the biometric prompt only once
    LaunchedEffect(promptShown) {
        if (!promptShown) {
            biometricManager.showBiometricPrompt("Authenticate", "To Log in to Penny")
            promptShown = true
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "",
                            )
                        }

                        Text("Penny")
                    }

                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (locked) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.LockOpen,
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Locked",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(80.dp))
            AsyncImage(
                model = user.profilePictureUrl,
                contentDescription = "",
                imageLoader = ImageLoader(
                    LocalContext.current
                ),
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(onClick = { promptShown = false }) {
                Text(text = "Authenticate")
            }
        }
    }
}
