package com.penny.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.penny.R
import com.penny.data.model.UserData
import com.penny.functions.greet
import com.penny.ui.component.FeaturedItem
import com.penny.ui.component.FixedHeightPreLoader
import com.penny.ui.component.RecentTransactions
import com.penny.ui.data.featuredList
import com.penny.ui.events.AccountsScreenEvents
import com.penny.ui.events.FeaturedScreenEvents
import com.penny.ui.events.TransactionScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.AccountsState
import com.penny.ui.state.FeaturedState
import com.penny.ui.state.TransactionState
import com.penny.ui.theme.White65
import com.penny.ui.viewModel.AccountsViewModel
import com.penny.ui.viewModel.FeaturedViewModel
import com.penny.ui.viewModel.TransactionViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    user: UserData,
    snackBarHostState: SnackbarHostState,
    navController: NavHostController,
    transactionState: TransactionState,
    featuredState: FeaturedState,
    transactionViewModel: TransactionViewModel,
    featuredViewModel: FeaturedViewModel,
    accountsState: AccountsState,
    accountsViewModel: AccountsViewModel
) {

    LaunchedEffect(key1 = Unit) {
        transactionViewModel.sendEvent(TransactionScreenEvents.LoadAllTransactions(uid = user.userId!!))
        featuredViewModel.sendEvent(FeaturedScreenEvents.GetFeatured(uid = user.userId))
        accountsViewModel.sendEvent(AccountsScreenEvents.FirstLoadGetAllAccounts(uid = user.userId))
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(vertical = 10.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(
                            greet(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            user.username?:"",
                            maxLines = 1,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraLight,
                            overflow = TextOverflow.Ellipsis,
                            color = White65,
                        )
                    }

                },
                actions = {
                    AsyncImage(
                        model = user.profilePictureUrl,
                        contentDescription = "User Profile",
                        modifier = Modifier.size(50.dp)
                            .clip(CircleShape)
                    )

                },
            )
        },
        bottomBar = { BottomBar(navController) }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "This Month",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(15.dp),
                fontWeight = FontWeight.Bold
            )
            if(featuredState.isLoading || accountsState.isLoading){
                FixedHeightPreLoader(height = 420.dp)
            }
            if (!featuredState.isLoading && featuredState.featured != null && !accountsState.isLoading) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 7.5.dp,
                        end = 7.5.dp,
                    ),
                    modifier = Modifier.height(420.dp)
                ) {
                    item {
                        featuredList[0].amount = featuredState.featured.income
                        FeaturedItem(feature = featuredList[0])
                    }
                    item {
                        featuredList[1].amount = featuredState.featured.expenses
                        FeaturedItem(feature = featuredList[1])
                    }
                    item {
                        val decimalFormat = DecimalFormat("#.##")
                        val formattedBalance = decimalFormat.format(accountsState.totalBalance)
                        featuredList[2].amount = formattedBalance
                        FeaturedItem(feature = featuredList[2])
                    }
                    item {
                        featuredList[3].amount = featuredState.featured.debt
                        FeaturedItem(feature = featuredList[3])
                    }
                }
            }
            RecentTransactions(navController, transactionState)
        }
    }
}
