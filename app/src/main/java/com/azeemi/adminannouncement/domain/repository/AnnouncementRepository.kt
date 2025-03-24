package com.azeemi.adminannouncement.domain.repository

import com.azeemi.adminannouncement.domain.model.Announcement
import com.azeemi.adminannouncement.domain.model.AnnouncementRequest
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {

    fun getAnnouncements(): Flow<List<Announcement>>
    suspend fun createAnnouncement(request: AnnouncementRequest): Announcement
    suspend fun deleteAnnouncement(id: Long)
}