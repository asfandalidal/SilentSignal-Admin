package com.azeemi.adminannouncement.domain.model

data class NotificationRequest(
    val title: String,
    val body: String,
    val expireAt:String
)