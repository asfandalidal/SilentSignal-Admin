package com.azeemi.adminannouncement.data.repository

import com.azeemi.adminannouncement.data.remote.ApiService
import com.azeemi.adminannouncement.domain.model.Announcement
import com.azeemi.adminannouncement.domain.model.AnnouncementRequest
import com.azeemi.adminannouncement.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class AnnouncementRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AnnouncementRepository {

    override fun getAnnouncements(): Flow<List<Announcement>> = flow {
        val announcements = apiService.getAnnouncements()
        emit(announcements)
    }.catch {
        emit(emptyList())
    }

    override suspend fun createAnnouncement(request: AnnouncementRequest): Announcement {
        return apiService.createAnnouncement(request)
    }

    override suspend fun deleteAnnouncement(id: Long) {
        apiService.deleteAnnouncement(id)
    }
}
