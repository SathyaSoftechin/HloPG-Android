package com.hlopg.data.api

import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.WeeklyFoodMenu
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodMenuApi {

    @GET("api/food_menu/{hostelId}")
    suspend fun getFoodMenu(
        @Path("hostelId") hostelId: Int
    ): Response<ApiResponse<WeeklyFoodMenu>>
}
