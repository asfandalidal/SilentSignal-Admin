package com.azeemi.adminannouncement.domain.usecase

import com.azeemi.adminannouncement.domain.model.AnnouncementRequest
import com.azeemi.adminannouncement.domain.model.NotificationRequest
import com.azeemi.adminannouncement.domain.repository.AnnouncementRepository
import com.azeemi.adminannouncement.domain.repository.NotificationRepository

class CreateAnnouncementUseCase(private val repository: AnnouncementRepository) {
    suspend operator fun invoke(request: AnnouncementRequest) = repository.createAnnouncement(request)
}

class GetAnnouncementsUseCase(private val repository: AnnouncementRepository) {
    operator fun invoke() = repository.getAnnouncements()
}

class DeleteAnnouncementUseCase(private val repository: AnnouncementRepository) {
    suspend operator fun invoke(id: String) = repository.deleteAnnouncement(id.toLong())
}

class SendNotificationUseCase(private val repository: NotificationRepository) {
    suspend operator fun invoke(request: NotificationRequest) = repository.sendNotification(request)
}