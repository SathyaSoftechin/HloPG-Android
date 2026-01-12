package com.hlopg.data.api

import com.hlopg.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    val BASE_URL = BuildConfig.BASE_URL  // base URL


    // Logging client
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    }

    // Single Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ----- API SERVICES -----

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val hostelApi: HostelApi by lazy {
        retrofit.create(HostelApi::class.java)
    }

    val foodMenuApi: FoodMenuApi by lazy {
        retrofit.create(FoodMenuApi::class.java)
    }

    val bookingApi: BookingApi by lazy {
        retrofit.create(BookingApi::class.java)
    }

    val ownerApi: OwnerApi by lazy {
        retrofit.create(OwnerApi::class.java)
    }

    val roomsApi: RoomsApi by lazy {
        retrofit.create(RoomsApi::class.java)
    }
}
