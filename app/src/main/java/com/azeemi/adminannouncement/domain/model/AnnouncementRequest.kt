package com.azeemi.adminannouncement.domain.model

data class AnnouncementRequest(
    val message: String,
    val expiresAt: String,
    val userId: Long = 1
)
