package com.penny.ui.screen

import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.penny.data.model.AnalysisItem
import com.penny.ui.component.Analysis
import com.penny.ui.component.EmptyTransactionAnalysis
import com.penny.ui.component.MonthYearPicker
import com.penny.ui.events.AnalysisScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.AnalysisState
import com.penny.ui.viewModel.AnalysisViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    uid: String,
    navController: NavHostController,
    analysisState: AnalysisState,
    viewModel: AnalysisViewModel
) {
    var visible by remember {
        mutableStateOf(false)
    }

    val calendar = Calendar.getInstance()
    val currMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based, add 1
    val currYear = calendar.get(Calendar.YEAR)

    var selectedMonth by remember { mutableIntStateOf(currMonth) }
    var selectedYear by remember { mutableIntStateOf(currYear) }

    LaunchedEffect(key1 = Unit) {
        viewModel.sendEvent(event = AnalysisScreenEvents.GetAnalysisItems(uid = uid))
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
                        "Analysis",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { visible = true }) {
                        Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = "")
                    }
                },
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

        if (!analysisState.isLoading) {
            val filteredItems= analysisState.analysisItems.filter { filter(it.month.toInt(),it.year.toInt(),selectedMonth,selectedYear) }
            if(filteredItems.isEmpty()){
                EmptyTransactionAnalysis()
            }else{
                val sortedItems=filteredItems.sortedBy { it.categoryId }
                val total = calculateTotalAmount(sortedItems)
                Analysis(
                    selectedMonth,
                    selectedYear,
                    innerPadding = innerPadding,
                    total = total,
                    analysisItems = sortedItems
                )
            }
        }
    }
}

fun calculateTotalAmount(analysisItems: List<AnalysisItem>): Double {
    var total = 0.0
    for (item in analysisItems) {
        total += item.amount.toDoubleOrNull() ?: 0.0
    }
    return total
}

fun filter(currMonth: Int,currYear: Int, selectedMonth: Int, selectedYear: Int): Boolean {
    return currYear==selectedYear && currMonth==selectedMonth
}

