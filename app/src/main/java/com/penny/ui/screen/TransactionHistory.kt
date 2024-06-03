package com.penny.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.penny.functions.filterMonthAndYear
import com.penny.ui.component.EmptyTransaction
import com.penny.ui.component.MonthYearPicker
import com.penny.ui.component.Preloader
import com.penny.ui.component.TransactionItem
import com.penny.ui.events.TransactionScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.TransactionState
import com.penny.ui.viewModel.TransactionViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistory(
    uid: String,
    navController: NavHostController,
    state: TransactionState,
    viewModel: TransactionViewModel
) {

    val calendar = Calendar.getInstance()
    val currMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based, add 1
    val currYear = calendar.get(Calendar.YEAR)

    var visible by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableIntStateOf(currMonth) }
    var selectedYear by remember { mutableIntStateOf(currYear) }

    // Load transactions when the composable first appears
    LaunchedEffect(Unit) {
        viewModel.sendEvent(event = TransactionScreenEvents.LoadAllTransactions(uid))
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
                        "Transactions",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { visible = true }) {
                        Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->

        MonthYearPicker(
            visible = visible,
            currentMonth = currMonth-1,
            currentYear = currYear,
            onConfirmation = { month, year ->
                selectedMonth = month
                selectedYear = year
                visible = false
            },
            onDismisRequest = {
                visible=false
            }
        )

        if (!state.isLoading) {
            val filteredTransactions = state.transactions.filter {
                filterMonthAndYear(it.timestamp, selectedMonth, selectedYear)
            }
            if(filteredTransactions.isEmpty()) {
                EmptyTransaction()
            }
            else {
                val sortedTransactions = filteredTransactions.sortedByDescending { it.timestamp }
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(sortedTransactions.size) { index ->
                        TransactionItem(navController = navController, transaction = sortedTransactions[index])
                    }
                }
            }

        } else {
            // Handle loading state here
            Preloader()
        }
    }
}