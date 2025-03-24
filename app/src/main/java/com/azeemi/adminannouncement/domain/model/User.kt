package com.azeemi.adminannouncement.domain.model

data class User(
    val id: Long,
    val username: String,
    val encryptedKey: String // Encrypted key for decryption
)