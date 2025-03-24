package com.azeemi.adminannouncement.data.remote

import com.azeemi.adminannouncement.domain.model.Announcement
import com.azeemi.adminannouncement.domain.model.AnnouncementRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @GET("active")
    suspend fun getAnnouncements(): List<Announcement>

    @POST("create")
    suspend fun createAnnouncement(@Body request: AnnouncementRequest): Announcement

    @DELETE("{id}")
    suspend fun deleteAnnouncement(@Path("id") id: Long)
}