package com.penny.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.penny.data.model.Transaction
import com.penny.ui.screen.Screen
import com.penny.ui.state.TransactionState
import com.penny.ui.theme.White80
import kotlin.math.min

@Composable
fun RecentTransactions(
    navController: NavHostController,
    transactionState: TransactionState
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(15.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Show all",
            style = MaterialTheme.typography.titleMedium,
            color = White80,
            modifier = Modifier
                .padding(15.dp)
                .clickable { navController.navigate(Screen.TransactionHistory.route) },
            fontWeight = FontWeight.ExtraLight,
            textDecoration = TextDecoration.Underline,
        )
    }
    if(transactionState.isLoading){
        FixedHeightPreLoader(height = 200.dp)
    }
    if(!transactionState.isLoading){
        val filteredTransaction= transactionState.transactions.sortedByDescending { it.timestamp }
        if(filteredTransaction.isEmpty()){
            NoRecentTransaction()
        }
        else{
            LazyColumn(
                modifier = Modifier.height(380.dp)
            ) {
                items(min(4,filteredTransaction.size)){
                    TransactionItem(navController = navController, transaction = filteredTransaction[it])
                }
            }
        }

    }

}