package com.penny.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.penny.data.model.AnalysisItem
import com.penny.ui.data.categoryList
import com.penny.ui.theme.LightGreen3

@SuppressLint("DefaultLocale")
@Composable
fun AnalysisItemComponent(
    item: AnalysisItem,
    total: Double
) {
    val percentage = (item.amount.toDouble() / total) * 100
    val formattedPercentage = String.format("%.2f", percentage) + "%"

    Card(
        modifier = Modifier.padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 5.dp)
        ) {
            Icon(
                painter = painterResource(id = categoryList[item.categoryId.toInt()].icon),
                contentDescription = "",
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(3.dp, categoryList[item.categoryId.toInt()].color),
                        CircleShape
                    )
                    .padding(10.dp)
            )
            Text(
                text = categoryList[item.categoryId.toInt()].categoryName,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = formattedPercentage,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .widthIn(max = 60.dp),
                color = LightGreen3,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = "")
            Text(
                text = item.amount,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .widthIn(max = 100.dp),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}