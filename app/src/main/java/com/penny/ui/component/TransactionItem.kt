package com.penny.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.penny.data.model.Transaction
import com.penny.functions.formatDate
import com.penny.ui.data.categoryList
import com.penny.ui.screen.Screen

@Composable
fun TransactionItem(
    navController: NavHostController,
    transaction: Transaction
){

    Card(
        modifier = Modifier.padding(5.dp).clickable{navController.navigate(Screen.TransactionDetails.withArgs(transaction.id))},
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
        ) {
            Icon(
                painter = painterResource(id = categoryList[transaction.categoryInd.toInt()].icon),
                contentDescription = "",
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(3.dp, categoryList[transaction.categoryInd.toInt()].color),
                        CircleShape
                    )
                    .padding(10.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp),
            ) {
                Text(
                    text = transaction.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 150.dp)
                )
                Text(
                    text = formatDate(transaction.timestamp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                )
            }

            Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = "")
            Text(
                text = transaction.amount,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .widthIn(max = 100.dp),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}