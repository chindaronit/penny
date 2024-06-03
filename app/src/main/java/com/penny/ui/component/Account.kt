package com.penny.ui.component


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.penny.data.model.Accounts
import com.penny.ui.data.PaymentSourcesList
import com.penny.ui.theme.White
import java.text.DecimalFormat

@Composable
fun Account(
    visible: Boolean,
    accounts: List<Accounts>,
    selected: String,
    onSelect: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.heightIn(max = 400.dp)
    ) {
        items(accounts.size) {
            Card(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { _ ->
                                onSelect(accounts[it].id)
                            }
                        )
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (selected == accounts[it].id) White else CardDefaults.cardColors().containerColor,
                    contentColor = if(selected==accounts[it].id) Color.Black else CardDefaults.cardColors().contentColor
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)

                ) {
                    Text(
                        text = PaymentSourcesList[accounts[it].sourceInd.toInt()].source,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Icon(
                        imageVector = Icons.Filled.CurrencyRupee,
                        contentDescription = "CurrencyRupee Icon"
                    )
                    if (visible) {
                        val decimalFormat = DecimalFormat("#.##")
                        val formattedBalance = decimalFormat.format(accounts[it].balance.toFloat())
                        Text(
                            text = formattedBalance,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .widthIn(max = 100.dp),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
            }
        }
    }
}