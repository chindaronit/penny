package com.penny.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.penny.ui.component.Account
import com.penny.ui.component.AddAccount
import com.penny.ui.component.EditAccount
import com.penny.ui.component.EmptyAccount
import com.penny.ui.data.PaymentSourcesList
import com.penny.ui.events.AccountsScreenEvents
import com.penny.ui.events.FeaturedScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.AccountsState
import com.penny.ui.state.FeaturedState
import com.penny.ui.theme.BlueViolet2
import com.penny.ui.theme.ButtonBlue
import com.penny.ui.theme.DarkGreen
import com.penny.ui.theme.OrangeYellow1
import com.penny.ui.viewModel.AccountsViewModel
import com.penny.ui.viewModel.FeaturedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    uid: String,
    snackBarHostState: SnackbarHostState,
    viewModel: AccountsViewModel,
    state: AccountsState,
    navController: NavHostController,
    featuredState: FeaturedState,
    featuredViewModel: FeaturedViewModel
) {

    var visible by remember {
        mutableStateOf(false)
    }

    var selectedAccount by remember {
        mutableStateOf("")
    }

    val addSheetState = rememberModalBottomSheetState()
    val addSheetScope = rememberCoroutineScope()
    var showAddBottomSheet by remember { mutableStateOf(false) }

    val editSheetState = rememberModalBottomSheetState()
    val editSheetScope = rememberCoroutineScope()
    var showEditBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.sendEvent(event = AccountsScreenEvents.FirstLoadGetAllAccounts(uid = uid))
        featuredViewModel.sendEvent(
            event = FeaturedScreenEvents.GetFeatured(
                uid = uid
            )
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        "Accounts",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { visible = !visible }
                    ) {
                        if (visible) {
                            Icon(
                                imageVector = Icons.Outlined.Visibility,
                                contentDescription = "visibility on",
                                modifier = Modifier.size(30.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.VisibilityOff,
                                contentDescription = "visibility off",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { showAddBottomSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircleOutline,
                            contentDescription = "Add new account",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = {
            if (selectedAccount.isNotEmpty()) {
                Row {
                    FloatingActionButton(
                        onClick = {
                            viewModel.sendEvent(
                                event = AccountsScreenEvents.DeleteAccount(
                                    id = selectedAccount,
                                    uid = uid
                                )
                            )

                            selectedAccount = ""
                        }
                    ) {
                        Icon(Icons.Filled.Delete, "Delete Account")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = { showEditBottomSheet = true },
                    ) {
                        Icon(Icons.Filled.Edit, "Edit Account")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (!state.isLoading && !featuredState.isLoading && featuredState.featured != null) {

            val bankAccounts =
                state.accounts.filter { PaymentSourcesList[it.sourceInd.toInt()].type == "Bank" }
            val cards =
                state.accounts.filter { PaymentSourcesList[it.sourceInd.toInt()].type == "Card" }
            val digitalWallets =
                state.accounts.filter { PaymentSourcesList[it.sourceInd.toInt()].type == "Digital Wallet" }

            AddAccount(
                uid,
                sheetState = addSheetState,
                scope = addSheetScope,
                viewModel = viewModel,
                showBottomSheet = showAddBottomSheet
            ) {
                showAddBottomSheet = it
            }

            if (selectedAccount.isNotEmpty()) {
                EditAccount(
                    uid = uid,
                    account = state.accounts.filter { it.id == selectedAccount }[0],
                    sheetState = editSheetState,
                    scope = editSheetScope,
                    showBottomSheet = showEditBottomSheet,
                    onEdit = {
                        viewModel.sendEvent(
                            event = AccountsScreenEvents.UpdateAccount(it.id, it)
                        )
                        selectedAccount = ""
                    },
                    toggleVisibility = {
                        showEditBottomSheet = it
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(start = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                AssistChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Cash") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Money,
                            contentDescription = "",
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                            tint = DarkGreen,
                        )
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
                Card(
                    modifier = Modifier
                        .padding(start = 7.5.dp, top = 10.dp, end = 7.5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DarkGreen
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Cash",
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            fontSize = 18.sp,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Icon(
                            imageVector = Icons.Filled.CurrencyRupee,
                            contentDescription = "CurrencyRupee Icon"
                        )
                        Text(
                            text = featuredState.featured.cash,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .widthIn(max = 100.dp),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Bank Accounts") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = "",
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                            tint = BlueViolet2
                        )
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
                if (bankAccounts.isNotEmpty()) {
                    Account(visible, bankAccounts, selectedAccount) {
                        selectedAccount = if (it == selectedAccount) ""
                        else {
                            it
                        }
                    }
                } else {
                    EmptyAccount(accountType = "Bank Account")
                }

                Spacer(modifier = Modifier.height(15.dp))
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Cards") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.CreditCard,
                            contentDescription = "",
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                            tint = OrangeYellow1,
                        )
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
                if (cards.isNotEmpty()) {
                    Account(visible, cards, selectedAccount) {
                        selectedAccount = if (it == selectedAccount) ""
                        else {
                            it
                        }
                    }
                } else {
                    EmptyAccount(accountType = "Cards")
                }

                Spacer(modifier = Modifier.height(15.dp))
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Digital Wallet") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.AccountBalanceWallet,
                            contentDescription = "",
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                            tint = ButtonBlue,
                        )
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
                if (digitalWallets.isNotEmpty()) {
                    Account(visible, digitalWallets, selectedAccount) {
                        selectedAccount = if (it == selectedAccount) ""
                        else {
                            it
                        }
                    }
                } else {
                    EmptyAccount(accountType = "Digital Wallet")
                }
            }
        }

    }
}
