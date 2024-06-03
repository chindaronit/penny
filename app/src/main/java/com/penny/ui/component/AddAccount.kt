package com.penny.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.penny.ui.mapping.paymentModeMapper
import com.penny.ui.viewModel.AccountsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccount(
    uid: String,
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: AccountsViewModel,
    showBottomSheet: Boolean,
    toggleVisibility: (Boolean) -> Unit
) {

    var balance by remember {
        mutableStateOf("")
    }

    var isPaymentModeExpanded by remember {
        mutableStateOf(false)
    }

    var isSourceExpanded by remember {
        mutableStateOf(false)
    }

    var type by remember {
        mutableStateOf("Bank")
    }

    var source by remember {
        mutableStateOf(PaymentSourcesList.filter { it.type == type })
    }

    var sourceInd by remember {
        mutableIntStateOf(0)
    }

    var selectedSource by remember {
        mutableStateOf(source[0].source)
    }


    LaunchedEffect(key1 = type) {
        source = PaymentSourcesList.filter { it.type == type }
    }

    LaunchedEffect(key1 = source) {
        sourceInd = source[0].ind
        selectedSource = source[0].source
    }

    if (showBottomSheet) {
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
                    text = "Add a new Account",
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
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = isPaymentModeExpanded,
                    onExpandedChange = { isPaymentModeExpanded = it }
                ) {
                    TextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPaymentModeExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isPaymentModeExpanded,
                        onDismissRequest = { isPaymentModeExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        paymentModeMapper.forEach { (key, value) ->
                            DropdownMenuItem(
                                text = {
                                    Text(value)
                                },
                                onClick = {
                                    type = value
                                    isPaymentModeExpanded = false
                                },
                            )
                            if (key < 2) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Source",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = isSourceExpanded,
                    onExpandedChange = { isSourceExpanded = it }
                ) {
                    TextField(
                        value = selectedSource,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSourceExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isSourceExpanded,
                        onDismissRequest = { isSourceExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        source.forEachIndexed { _, paymentSource ->
                            DropdownMenuItem(
                                text = {
                                    Text(paymentSource.source)
                                },
                                onClick = {
                                    sourceInd = paymentSource.ind
                                    selectedSource = paymentSource.source
                                    isSourceExpanded = false
                                },
                            )

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = balance,
                    singleLine = true,
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
                if (balance.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {

                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    toggleVisibility(false)
                                }
                            }

                            viewModel.sendEvent(
                                event = AccountsScreenEvents.AddAccount(
                                    uid = uid,
                                    account = Accounts(
                                        uid=uid,
                                        sourceInd = sourceInd.toLong(),
                                        balance = balance
                                    )
                                )
                            )

                        }
                    ) {
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }
}