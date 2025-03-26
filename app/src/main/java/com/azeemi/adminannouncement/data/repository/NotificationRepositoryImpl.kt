package com.azeemi.adminannouncement.data.repository

import com.azeemi.adminannouncement.data.remote.ApiService
import com.azeemi.adminannouncement.domain.model.NotificationRequest
import com.azeemi.adminannouncement.domain.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NotificationRepository {

    override suspend fun sendNotification(request: NotificationRequest): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.sendNotification(request)
                if (response.isSuccessful) {
                    Result.success(response.body()?.string() ?: "Notification sent successfully")
                } else {
                    Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}