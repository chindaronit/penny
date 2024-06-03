package com.penny.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.penny.R

@Composable
fun NoRecentTransaction(
    modifier: Modifier=Modifier
){
    Card(
        modifier = modifier
            .padding(start = 10.dp, top = 10.dp, end = 10.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column {
            Text(
                text = "No Transaction Found...",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            AsyncImage(
                model = R.drawable.emptytransaction,
                contentDescription = "",
                imageLoader = ImageLoader(
                    LocalContext.current
                ),
                modifier = modifier.size(200.dp).align(Alignment.CenterHorizontally)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(15.dp)

            ) {
                Icon(imageVector= Icons.Filled.Add, contentDescription="")
                Text(
                    text = "Add an expense",
                    modifier = modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

    }
}