package com.hlopg.di


import com.hlopg.data.api.HostelApi
import com.hlopg.data.api.AuthApi
import com.hlopg.data.api.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHostelApi(): HostelApi {
        return RetrofitInstance.hostelApi
    }

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return RetrofitInstance.authApi
    }
}
