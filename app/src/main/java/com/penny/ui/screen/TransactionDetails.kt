package com.penny.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.penny.functions.formatDate
import com.penny.functions.formatTime
import com.penny.ui.data.PaymentSourcesList
import com.penny.ui.data.categoryList
import com.penny.ui.events.TransactionScreenEvents
import com.penny.ui.navigation.BottomBar
import com.penny.ui.state.TransactionState
import com.penny.ui.theme.White80
import com.penny.ui.viewModel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetails(
    navController: NavHostController,
    transactionState: TransactionState,
    transactionViewModel: TransactionViewModel,
    transactionId: String
) {
    LaunchedEffect(key1 = Unit) {
        transactionViewModel.sendEvent(event = TransactionScreenEvents.GetTransaction(id = transactionId))
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
                        "Transaction Detail",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(25.dp)
                    )
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) }

    ) { innerPadding ->
        if(!transactionState.isLoading && transactionState.selectedTransaction!=null){
            val transaction=transactionState.selectedTransaction

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        painter = painterResource(id = categoryList[transaction.categoryInd.toInt()].icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 50.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(3.dp, categoryList[transaction.categoryInd.toInt()].color),
                                CircleShape
                            )
                            .padding(15.dp)

                    )

                    Text(
                        text = "Category : "+categoryList[transaction.categoryInd.toInt()].categoryName,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = categoryList[transaction.categoryInd.toInt()].color
                    )

                    Text(
                        text = "Title : "+transaction.title,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Date : "+ formatDate(transaction.timestamp),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = White80
                    )

                    Text(
                        text = "Time : "+ formatTime(transaction.timestamp),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = White80
                    )
                    if(transaction.paymentSourceInd.toInt()!=-1){
                        Text(
                            text = "Payment Mode : "+ PaymentSourcesList[transaction.paymentSourceInd.toInt()].type,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Text(
                            text = "Source : "+PaymentSourcesList[transaction.paymentSourceInd.toInt()].source,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    else{
                        Text(
                            text = "Payment Mode : Cash",
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Text(
                        text = "Description : "+transaction.description,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                }
            }
        }

    }
}