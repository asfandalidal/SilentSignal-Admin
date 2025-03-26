package com.azeemi.adminannouncement.domain.repository



import com.azeemi.adminannouncement.domain.model.NotificationRequest

interface NotificationRepository {
    suspend fun sendNotification(request: NotificationRequest): Result<String>
}
