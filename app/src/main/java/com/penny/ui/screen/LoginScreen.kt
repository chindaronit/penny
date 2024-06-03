package com.penny.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.penny.R
import com.penny.ui.state.SignInState
import com.penny.ui.theme.White65

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: ()->Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        text = "Welcome",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.padding(end = 5.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(painterResource(id = R.drawable.logo), contentDescription = "")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.managemoney,
                contentDescription = "",
                imageLoader = ImageLoader(context = LocalContext.current),
                modifier = Modifier
                    .size(320.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Get Started, with Penny",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
            )
            Text(
                text = "Let's manage your money, Efficiently!",
                fontWeight = FontWeight.ExtraLight,
                color = White65,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp),
            )
            Spacer(modifier = Modifier.height(50.dp))
            Surface(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        onSignInClick()
                    },
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 16.dp,
                            top = 12.dp,
                            bottom = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    AsyncImage(
                        model = R.drawable.ic_google_logo,
                        contentDescription = "",
                        imageLoader = ImageLoader(
                            LocalContext.current
                        ),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Continue with Google")
                }

            }
        }
    }
}
