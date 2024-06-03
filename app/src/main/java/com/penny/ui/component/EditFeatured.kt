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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import com.penny.data.model.Featured
import com.penny.ui.events.FeaturedScreenEvents
import com.penny.ui.viewModel.FeaturedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFeatured(
    modifier: Modifier=Modifier,
    uid: String,
    featured: Featured,
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: FeaturedViewModel,
    showBottomSheet: Boolean,
    toggleVisibility: (Boolean) -> Unit,
) {

    if (showBottomSheet) {
        var income by remember {
            mutableStateOf(featured.income)
        }

        var debt by remember {
            mutableStateOf(featured.debt)
        }

        var cash by remember {
            mutableStateOf(featured.cash)
        }

        ModalBottomSheet(
            onDismissRequest = {
                toggleVisibility(false)
            },
            sheetState = sheetState,
        ) {
            // Sheet content
            Column(
                modifier = modifier.padding(10.dp)
            ) {
                Text(
                    text = "Edit Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = modifier.height(20.dp))
                OutlinedTextField(
                    value =income,
                    onValueChange = { val floatValue = it.toFloatOrNull()
                        if (floatValue != null && floatValue >= 0 || it.isEmpty()) {
                            income = it
                        }},
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    label = { Text(text = "Income")},
                    modifier = modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = modifier.height(8.dp))
                OutlinedTextField(
                    value =debt,
                    onValueChange = { val floatValue = it.toFloatOrNull()
                        if (floatValue != null && floatValue >= 0 || it.isEmpty()) {
                            debt = it
                        } },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    label = { Text(text = "Debt")},
                    modifier = modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = modifier.height(8.dp))
                OutlinedTextField(
                    value =cash,
                    onValueChange = { val floatValue = it.toFloatOrNull()
                        if (floatValue != null && floatValue >= 0 || it.isEmpty()) {
                            cash = it
                        } },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    label = { Text(text = "Cash") },
                    modifier = modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = modifier.height(8.dp))
                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                toggleVisibility(false)
                            }
                        }
                        viewModel.sendEvent(FeaturedScreenEvents.UpdateFeatured(
                            uid=uid,
                            Featured(
                                id = featured.id,
                                uid = uid,
                                income=income,
                                debt = debt,
                                cash = cash,
                                expenses = featured.expenses,
                            ),
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