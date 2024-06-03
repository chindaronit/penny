package com.penny.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.penny.data.model.AnalysisItem
import com.penny.data.model.Featured
import com.penny.data.model.Transaction
import com.penny.functions.convertTimestampToMonthYear
import com.penny.functions.formatDate
import com.penny.ui.component.DatePickerButton
import com.penny.ui.component.Dialog
import com.penny.ui.component.Preloader
import com.penny.ui.data.PaymentSourcesList
import com.penny.ui.data.categoryList
import com.penny.ui.events.AccountsScreenEvents
import com.penny.ui.events.AnalysisScreenEvents
import com.penny.ui.events.FeaturedScreenEvents
import com.penny.ui.events.TransactionScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.AccountsState
import com.penny.ui.state.FeaturedState
import com.penny.ui.theme.Red1
import com.penny.ui.theme.White
import com.penny.ui.viewModel.AccountsViewModel
import com.penny.ui.viewModel.AnalysisViewModel
import com.penny.ui.viewModel.FeaturedViewModel
import com.penny.ui.viewModel.TransactionViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    uid: String,
    snackBarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: TransactionViewModel,
    analysisViewModel: AnalysisViewModel,
    featuredState: FeaturedState,
    featuredViewModel: FeaturedViewModel,
    accountsState: AccountsState,
    accountsViewModel: AccountsViewModel
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1

    var cashChecked by remember { mutableStateOf(false) }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var selectedMonth by remember {
        mutableIntStateOf(month)
    }

    var selectedYear by remember {
        mutableIntStateOf(year)
    }

    var categoryInd by remember {
        mutableIntStateOf(0)
    }

    var amount by remember {
        mutableStateOf("")
    }

    var accounts by remember {
        mutableStateOf(accountsState.accounts)
    }

    var timestamp by remember {
        mutableStateOf(Timestamp.now())
    }

    var date by remember {
        mutableStateOf(formatDate(Timestamp.now()))
    }

    var isPaymentModeExpanded by remember {
        mutableStateOf(false)
    }

    var isCategoryExpanded by remember {
        mutableStateOf(false)
    }

    var isSourceExpanded by remember {
        mutableStateOf(false)
    }

    var featured by remember {
        mutableStateOf(featuredState.featured)
    }


    LaunchedEffect(key1 = Unit) {
        featuredViewModel.sendEvent(
            event = FeaturedScreenEvents.GetFeatured(
                uid = uid
            )
        )
        accountsViewModel.sendEvent(event = AccountsScreenEvents.FirstLoadGetAllAccounts(uid = uid))
    }

    LaunchedEffect(key1 = accountsState.accounts, key2 = accountsState.isLoading) {
        if (!accountsState.isLoading) {
            accounts = accountsState.accounts
        }
    }

    LaunchedEffect(key1 = featuredState.featured) {
        if (featuredState.featured != null) {
            featured = featuredState.featured
        }
    }

    if (featuredState.isLoading || accountsState.isLoading) {
        Preloader()
    } else if (accounts.isEmpty()) {

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
                            "Add an Expense",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                )
            },
            bottomBar = { BottomBar(navController) }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {}
            Dialog(
                onDismissRequest = { navController.navigate(Screen.MainScreen.route) },
                onConfirmation = { navController.navigate(Screen.AccountsScreen.route) },
                dialogTitle = "Add an account first",
                dialogText = "You first have to add an account to continue",
                icon = Icons.Filled.Add
            )
        }

    } else if (featured != null) {

        val typeList by remember {
            // Extract and filter the types from accounts
            val uniqueTypes = mutableSetOf<String>()
            for (account in accounts) {
                uniqueTypes.add(PaymentSourcesList[account.sourceInd.toInt()].type)
            }

            mutableStateOf(uniqueTypes.toList())
        }

        var selectedType by remember {
            mutableStateOf(typeList[0])
        }

        var selectedTypeEntry by remember {
            mutableStateOf(accounts.filter { PaymentSourcesList[it.sourceInd.toInt()].type == selectedType })
        }

        var selectedSource by remember {
            mutableStateOf(PaymentSourcesList[selectedTypeEntry[0].sourceInd.toInt()].source)
        }

        var paymentSourceInd by remember {
            mutableIntStateOf(PaymentSourcesList[selectedTypeEntry[0].sourceInd.toInt()].ind)
        }

        var isAllowed by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(key1 = amount, key2 = cashChecked) {
            if (accounts.isNotEmpty() && amount.isNotBlank() && !cashChecked) {
                val balance =
                    accounts.filter { it.sourceInd.toInt() == paymentSourceInd }[0].balance
                isAllowed = if (amount.toFloat() > balance.toFloat()) {
                    false
                } else {
                    true
                }
            } else if (cashChecked && accounts.isNotEmpty() && amount.isNotBlank()) {
                val balance = featured?.cash ?: "0"
                isAllowed = if (amount.toFloat() > balance.toFloat()) {
                    false
                } else {
                    true
                }
            }
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
                            "Add an Expense",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        if (title.isNotEmpty() && amount.isNotEmpty() && isAllowed) {
                            IconButton(onClick = {
                                analysisViewModel.sendEvent(
                                    event = AnalysisScreenEvents.AddOrUpdateAnalysisItem(
                                        uid = uid,
                                        AnalysisItem(
                                            uid = uid,
                                            month = selectedMonth.toLong(),
                                            year = selectedYear.toLong(),
                                            amount = amount,
                                            categoryId = categoryInd.toLong()
                                        )
                                    )
                                )

                                if (cashChecked) {
                                    viewModel.sendEvent(
                                        event = TransactionScreenEvents.AddTransaction(
                                            uid = uid,
                                            transaction =
                                            Transaction(
                                                uid = uid,
                                                title = title,
                                                amount = amount,
                                                timestamp = timestamp,
                                                categoryInd = categoryInd.toLong(),
                                                paymentSourceInd = -1,
                                                description = description
                                            )
                                        )
                                    )
                                }


                                if (!cashChecked) {
                                    accountsViewModel.sendEvent(
                                        AccountsScreenEvents.ReduceAccountBalanceWithSourceInd(
                                            uid = uid,
                                            paymentSourceInd.toLong(),
                                            amount.toDouble()
                                        )
                                    )
                                    viewModel.sendEvent(
                                        event = TransactionScreenEvents.AddTransaction(
                                            uid = uid,
                                            transaction =
                                            Transaction(
                                                uid = uid,
                                                title = title,
                                                amount = amount,
                                                timestamp = timestamp,
                                                categoryInd = categoryInd.toLong(),
                                                paymentSourceInd = paymentSourceInd.toLong(),
                                                description = description
                                            )
                                        )
                                    )
                                }

                                if (selectedMonth == month && selectedYear == year && !cashChecked) {
                                    val currentExpense = featured?.expenses?.toFloatOrNull() ?: 0f
                                    val updatedExpense = currentExpense + amount.toFloat()

                                    // Dispatch the event
                                    featuredViewModel.sendEvent(
                                        FeaturedScreenEvents.WithoutReloadUpdateFeatured(
                                            uid = uid,
                                            Featured(
                                                id = featured?.id ?: "",
                                                uid = uid,
                                                income = featured?.income ?: "0",
                                                expenses = updatedExpense.toString(),
                                                debt = featured?.debt ?: "0",
                                                cash = featured?.cash ?: "0",
                                            )
                                        )
                                    )
                                } else if (cashChecked && selectedMonth == month && selectedYear == year) {
                                    val currentCash = featured?.cash?.toFloat() ?: 0f
                                    val updatedCash = currentCash - amount.toFloat()
                                    val currentExpense = featured?.expenses?.toFloatOrNull() ?: 0f
                                    val updatedExpense = currentExpense + amount.toFloat()

                                    featuredViewModel.sendEvent(
                                        FeaturedScreenEvents.WithoutReloadUpdateFeatured(
                                            uid = uid,
                                            Featured(
                                                id = featured?.id ?: "",
                                                uid = uid,
                                                income = featured?.income ?: "0",
                                                expenses = updatedExpense.toString(),
                                                debt = featured?.debt ?: "0",
                                                cash = updatedCash.toString(),
                                            )
                                        )
                                    )
                                }


                                navController.navigate(Screen.MainScreen.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Add expense"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = { BottomBar(navController) }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = White,
                        focusedBorderColor = White,
                        cursorColor = White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = TextStyle(
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title", color = White) }
                )
                Spacer(modifier = Modifier.height(25.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = amount,
                    onValueChange = {
                        val floatValue = it.toFloatOrNull()
                        if (floatValue != null && floatValue >= 0 || it.isEmpty()) {
                            amount = it
                        }
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (!isAllowed) {
                    Text(
                        text = "Insufficient Funds in selected Account...",
                        style = MaterialTheme.typography.titleMedium,
                        color = Red1,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DatePickerButton {
                        val (newMonth, newYear) = convertTimestampToMonthYear(it)
                        selectedMonth = newMonth
                        selectedYear = newYear
                        date = formatDate(it)
                        timestamp = it
                    }
                    Text(text = date)
                }
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = isCategoryExpanded,
                    onExpandedChange = { isCategoryExpanded = it }
                ) {
                    TextField(
                        value = categoryList[categoryInd].categoryName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categoryList.forEachIndexed { index, category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        category.categoryName,
                                    )
                                },
                                onClick = {
                                    categoryInd = index
                                    isCategoryExpanded = false
                                },
                            )
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                            ) // Add the Divider here
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cash",
                        modifier = Modifier.padding(end = 10.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Switch(
                        checked = cashChecked,
                        onCheckedChange = {
                            cashChecked = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        )
                    )
                }
                if (!cashChecked) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Payment Mode",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = isPaymentModeExpanded,
                        onExpandedChange = { isPaymentModeExpanded = it }
                    ) {
                        TextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPaymentModeExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isPaymentModeExpanded,
                            onDismissRequest = { isPaymentModeExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            typeList.forEachIndexed { _, type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(type)
                                    },
                                    onClick = {
                                        selectedType = type
                                        selectedTypeEntry =
                                            accounts.filter { PaymentSourcesList[it.sourceInd.toInt()].type == selectedType }
                                        selectedSource =
                                            PaymentSourcesList[selectedTypeEntry[0].sourceInd.toInt()].source
                                        paymentSourceInd =
                                            PaymentSourcesList[selectedTypeEntry[0].sourceInd.toInt()].ind
                                        isPaymentModeExpanded = false
                                    },
                                )
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Source",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )

                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = isSourceExpanded,
                        onExpandedChange = { isSourceExpanded = it }
                    ) {
                        TextField(
                            value = selectedSource,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSourceExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isSourceExpanded,
                            onDismissRequest = { isSourceExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            selectedTypeEntry.forEachIndexed { _, entry ->
                                DropdownMenuItem(
                                    text = {
                                        Text(PaymentSourcesList[entry.sourceInd.toInt()].source)
                                    },
                                    onClick = {
                                        selectedSource =
                                            PaymentSourcesList[entry.sourceInd.toInt()].source
                                        paymentSourceInd =
                                            PaymentSourcesList[entry.sourceInd.toInt()].ind
                                        isSourceExpanded = false
                                    },
                                )

                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    label = { },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
            }
        }
    }
}
