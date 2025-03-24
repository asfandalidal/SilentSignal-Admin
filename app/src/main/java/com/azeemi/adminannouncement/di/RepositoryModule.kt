package com.azeemi.adminannouncement.di

import com.azeemi.adminannouncement.data.repository.AnnouncementRepositoryImpl
import com.azeemi.adminannouncement.domain.repository.AnnouncementRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnnouncementRepository(
        impl: AnnouncementRepositoryImpl
    ): AnnouncementRepository
}
