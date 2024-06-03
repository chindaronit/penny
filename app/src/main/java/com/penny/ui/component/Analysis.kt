package com.penny.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.penny.data.model.AnalysisItem
import com.penny.ui.theme.BlueViolet3
import com.penny.ui.theme.White

@Composable
fun Analysis(
    month: Int,
    year: Int,
    innerPadding: PaddingValues,
    total: Double,
    analysisItems: List<AnalysisItem>,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        PieChart(
            month = month,
            year=year,
            data = analysisItems
        )

        // Card displaying total amount spent
        Card(
            modifier = Modifier.padding(start = 12.dp, top = 25.dp, end = 12.dp, bottom = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = BlueViolet3
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Wallet,
                    contentDescription = "",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(3.dp, White),
                            CircleShape
                        )
                        .padding(10.dp)
                )
                Text(
                    text = "Total Amount Spent",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = "")
                Text(
                    text = total.toString(),
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .widthIn(max = 125.dp),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp),
            modifier = Modifier.heightIn(max = 800.dp)
        ) {
            items(analysisItems.size){
                AnalysisItemComponent(item = analysisItems[it], total = total)
            }
        }
    }
}

