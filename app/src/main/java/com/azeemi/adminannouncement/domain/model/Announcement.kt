package com.azeemi.adminannouncement.domain.model

data class Announcement(
    val id: Long,
    val message: String,
    val createdAt: String,
    val expiresAt: String,
    val user: User
)