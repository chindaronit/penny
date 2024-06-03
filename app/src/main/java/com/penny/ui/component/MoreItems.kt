package com.penny.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun MoreItem(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    amount: String,
) {

    Card(
        modifier = modifier
            .padding(start = 25.dp, end = 25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)

        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = modifier.size(30.dp)
            )

            Text(
                text = text,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
            )

            Icon(
                imageVector = Icons.Filled.CurrencyRupee,
                contentDescription = "CurrencyRupee Icon"
            )

            Text(
                text = amount,
                modifier = modifier
                    .padding(end = 10.dp)
                    .widthIn(max = 100.dp),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}