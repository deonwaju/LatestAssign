package com.deontch.common

import android.content.Context
import android.widget.Toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun String.cleanMessage(): String {
    val parts = this.split(" ")
    // Ensure the message starts with HTTP and contains a 3-digit code before the message
    val errorCode = parts.getOrNull(1) ?: "XXX"  // Default to "XXX" if the second part is missing
    return if (parts.firstOrNull() == "HTTP" && errorCode.matches(Regex("\\d{3}"))) {
        parts.drop(2).joinToString(" ")
    } else {
        this  // Return the original string if it doesn't meet the condition
    }
}

fun Int.toRoman(): String {
    val numerals = listOf(
        1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
        100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
        10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
    )
    var num = this
    val result = StringBuilder()
    for ((value, symbol) in numerals) {
        while (num >= value) {
            result.append(symbol)
            num -= value
        }
    }
    return result.toString()
}
