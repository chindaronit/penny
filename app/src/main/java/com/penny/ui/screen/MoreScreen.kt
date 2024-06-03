package com.penny.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.penny.R
import com.penny.data.model.UserData
import com.penny.ui.component.Dialog
import com.penny.ui.component.EditFeatured
import com.penny.ui.component.MoreItem
import com.penny.ui.component.Preloader
import com.penny.ui.events.FeaturedScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.FeaturedState
import com.penny.ui.state.SignInState
import com.penny.ui.theme.BlueViolet3
import com.penny.ui.theme.LightGreen3
import com.penny.ui.theme.OrangeYellow2
import com.penny.ui.theme.OrangeYellow3
import com.penny.ui.theme.Pink40
import com.penny.ui.theme.Pink80
import com.penny.ui.theme.Red1
import com.penny.ui.theme.Red3
import com.penny.ui.viewModel.FeaturedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    user: UserData,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    featuredState: FeaturedState,
    featuredViewModel: FeaturedViewModel,
    onSignOut: ()->Unit
) {

    var toggleAboutUs by remember {
        mutableStateOf(false)
    }

    var viewDialog by remember {
        mutableStateOf(false)
    }

    val editSheetState = rememberModalBottomSheetState()
    val editSheetScope = rememberCoroutineScope()
    var showEditBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        featuredViewModel.sendEvent(event = FeaturedScreenEvents.GetFeatured(uid = user.userId!!))
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        "More",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showEditBottomSheet=true }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Featured",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->



        if (!featuredState.isLoading && featuredState.featured != null) {

            if (viewDialog) {
                Dialog(
                    onDismissRequest = { viewDialog = false },
                    onConfirmation = {
                        viewDialog = false
                        onSignOut()
                                     },
                    dialogTitle = "Are you sure?",
                    dialogText = "After confirmation, You will be directed to login screen",
                    icon = Icons.AutoMirrored.Filled.Logout
                )
            }

            EditFeatured(
                uid = user.userId!!,
                featured = featuredState.featured,
                sheetState =editSheetState,
                scope = editSheetScope,
                viewModel =featuredViewModel,
                showBottomSheet =showEditBottomSheet,
                toggleVisibility =  {
                    showEditBottomSheet=it
                }
            )

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = modifier.height(20.dp))
                AsyncImage(
                    model = user.profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = user.username?:"",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = modifier.height(30.dp))
                MoreItem(icon = R.drawable.down, text = "Income", amount = featuredState.featured.income)
                HorizontalDivider(modifier=Modifier.padding(horizontal = 25.dp))
                MoreItem(icon = R.drawable.creditor, text = "Debt", amount = featuredState.featured.debt)
                HorizontalDivider(modifier=Modifier.padding(horizontal = 25.dp))
                MoreItem(icon = R.drawable.money, text = "Cash", amount = featuredState.featured.cash)

                Card(
                    modifier = modifier
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                        .clickable { toggleAboutUs = !toggleAboutUs },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "About Us",
                                modifier = modifier.size(30.dp)
                            )

                            Text(
                                text = "About Us",
                                modifier = modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                            )

                            if (toggleAboutUs) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow),
                                    contentDescription = "Incoming Icon",
                                    modifier = modifier.size(30.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.down),
                                    contentDescription = "Incoming Icon",
                                    modifier = modifier.size(30.dp)
                                )
                            }

                        }
                        if (toggleAboutUs) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(start = 35.dp, end = 35.dp, bottom = 20.dp)
                            ) {
                                Text(
                                    text = "Welcome to Penny, developed by \"Ronit Chinda\". Our expense tracker simplifies managing your finances, helping you track spending, Analyse it, and stay in control. Designed for ease and efficiency.",
                                )
                            }

                        }
                    }
                }
                Card(
                    modifier = modifier
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                        .clickable { viewDialog = true },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sign Out",
                            modifier = modifier
                                .size(30.dp)
                                .rotate(180f)
                        )

                        Text(
                            text = "Sign Out",
                            modifier = modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                        )

                    }
                }
            }

        }
    }
}
