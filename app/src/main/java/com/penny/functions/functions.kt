package com.penny.functions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.time.LocalTime
import java.util.Calendar


fun formatDate(timestamp: Timestamp): String {
    val formatter = SimpleDateFormat("dd MMMM, yyyy (EEEE)", Locale.getDefault())
    val date = timestamp.toDate()  // Convert Firebase Timestamp to Date
    return formatter.format(date)  // Format the Date
}

fun formatTime(timestamp: Timestamp): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = timestamp.toDate()  // Convert Firebase Timestamp to Date
    return formatter.format(date)  // Format the Date to time in AM/PM
}

fun filterMonthAndYear(timestamp: Timestamp, selectedMonth: Int, selectedYear: Int): Boolean {
    val calendar = Calendar.getInstance().apply {
        time = timestamp.toDate()
    }
    val transactionMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based, so add 1
    val transactionYear = calendar.get(Calendar.YEAR)
    return transactionMonth == selectedMonth && transactionYear == selectedYear
}

fun convertTimestampToMonthYear(timestamp: Timestamp): Pair<Int, Int> {
    val calendar = Calendar.getInstance().apply {
        time = timestamp.toDate()
    }
    val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
    val year = calendar.get(Calendar.YEAR)
    return Pair(month, year)
}


fun greet(): String {
    val currentTime = LocalTime.now()
    val morningStart = LocalTime.of(0, 0)
    val noonStart = LocalTime.of(12, 0)
    val eveningStart = LocalTime.of(18, 0)

    return when {
        currentTime.isAfter(morningStart) && currentTime.isBefore(noonStart) -> "Good Morning,"
        currentTime.isAfter(noonStart) && currentTime.isBefore(eveningStart) -> "Good Noon,"
        else -> "Good Evening,"
    }
}
