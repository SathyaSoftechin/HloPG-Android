package com.hlopg.data.model

data class DailyMeal(
    val breakfast: String?,
    val lunch: String?,
    val dinner: String?
)

data class WeeklyFoodMenu(
    val monday: DailyMeal,
    val tuesday: DailyMeal,
    val wednesday: DailyMeal,
    val thursday: DailyMeal,
    val friday: DailyMeal,
    val saturday: DailyMeal,
    val sunday: DailyMeal
)
