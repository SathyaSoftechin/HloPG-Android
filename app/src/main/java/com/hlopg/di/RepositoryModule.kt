package com.hlopg.di

import com.hlopg.data.repository.HostelRepositoryImpl
import com.hlopg.domain.repository.HostelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHostelRepository(
        impl: HostelRepositoryImpl
    ): HostelRepository
}
