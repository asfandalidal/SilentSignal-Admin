package com.azeemi.adminannouncement.domain.model

data class NotificationRequest(
    val title: String,
    val body: String,
//    val userId: Long = 1
)