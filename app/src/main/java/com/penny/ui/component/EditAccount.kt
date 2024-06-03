package com.penny.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.penny.data.model.Accounts
import com.penny.ui.data.PaymentSourcesList
import com.penny.ui.events.AccountsScreenEvents
import com.penny.ui.viewModel.AccountsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccount(
    uid: String,
    account: Accounts,
    sheetState: SheetState,
    scope: CoroutineScope,
    showBottomSheet: Boolean,
    toggleVisibility: (Boolean) -> Unit,
    onEdit: (Accounts)->Unit
) {

    if (showBottomSheet) {
        var balance by remember {
            mutableStateOf(account.balance)
        }

        ModalBottomSheet(
            onDismissRequest = {
                toggleVisibility(false)
            },
            sheetState = sheetState,
        ) {
            // Sheet content
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Edit Account",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Payment Mode",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = PaymentSourcesList[account.sourceInd.toInt()].type,
                    onValueChange = {},
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Source",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = PaymentSourcesList[account.sourceInd.toInt()].source,
                    onValueChange = {},
                    readOnly = true,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = balance,
                    onValueChange = {
                        val floatValue = it.toFloatOrNull()
                        if (floatValue != null && floatValue >= 0 || it.isEmpty()) {
                            balance = it
                        }
                    },
                    label = { Text(text = "Balance") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                toggleVisibility(false)
                            }
                        }
                        onEdit(
                            Accounts(
                            id = account.id,
                            uid=uid,
                            sourceInd = account.sourceInd,
                            balance=balance
                        ))

                    },
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}