package com.hlopg.utils

fun getCurrentMonth(): String {
    val calendar = java.util.Calendar.getInstance()
    val year = calendar.get(java.util.Calendar.YEAR)
    val month = calendar.get(java.util.Calendar.MONTH) + 1
    return String.format("%04d-%02d", year, month)
}

fun getPreviousMonth(monthsAgo: Int): String {
    val calendar = java.util.Calendar.getInstance()
    calendar.add(java.util.Calendar.MONTH, -monthsAgo)
    val year = calendar.get(java.util.Calendar.YEAR)
    val month = calendar.get(java.util.Calendar.MONTH) + 1
    return String.format("%04d-%02d", year, month)
}