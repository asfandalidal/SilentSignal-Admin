package com.azeemi.adminannouncement.di

import com.azeemi.adminannouncement.domain.repository.AnnouncementRepository
import com.azeemi.adminannouncement.domain.repository.NotificationRepository
import com.azeemi.adminannouncement.domain.usecase.CreateAnnouncementUseCase
import com.azeemi.adminannouncement.domain.usecase.DeleteAnnouncementUseCase
import com.azeemi.adminannouncement.domain.usecase.GetAnnouncementsUseCase
import com.azeemi.adminannouncement.domain.usecase.SendNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetAnnouncementsUseCase(repository: AnnouncementRepository): GetAnnouncementsUseCase {
        return GetAnnouncementsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateAnnouncementUseCase(repository: AnnouncementRepository): CreateAnnouncementUseCase {
        return CreateAnnouncementUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteAnnouncementUseCase(repository: AnnouncementRepository): DeleteAnnouncementUseCase {
        return DeleteAnnouncementUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSendNotificationUseCase(repository: NotificationRepository): SendNotificationUseCase {
        return SendNotificationUseCase(repository)
    }
}
