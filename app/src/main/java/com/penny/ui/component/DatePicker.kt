package com.penny.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.penny.ui.theme.White
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Timestamp
import java.util.Date

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthYearPicker(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    onConfirmation: (Int, Int) -> Unit,
    onDismisRequest: () -> Unit
) {

    val months = listOf(
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    var month by remember {
        mutableStateOf(months[currentMonth])
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var year by remember {
        mutableIntStateOf(currentYear)
    }

    if (visible) {
        AlertDialog(
            icon = {

            },
            title = {
                Text(text = "Filter")
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year--
                                    }
                                ),
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = year.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year++
                                    }
                                ),
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )

                    }


                    Card(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = White
                        )
                    ) {

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.Center
                        ) {

                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                month = it
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(
                                                color = if (month == it) Color.Black else Color.Transparent,
                                                shape = CircleShape
                                            )
                                    )

                                    Text(
                                        text = it,
                                        color = if (month == it) Color.White else Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )

                                }
                            }

                        }

                    }

                }

            },
            onDismissRequest = {
                onDismisRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation(
                            months.indexOf(month) + 1,
                            year
                        )
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismisRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}


@Composable
fun DatePickerButton(
    onDateSelected: (Timestamp) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val date = calendar.time
            onDateSelected(Timestamp(date))
        },
        year,
        month,
        day
    )

    IconButton(onClick = { datePickerDialog.show() }) {
        Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = "")
    }
}
