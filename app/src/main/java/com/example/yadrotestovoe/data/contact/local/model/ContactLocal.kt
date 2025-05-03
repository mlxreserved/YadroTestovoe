package com.example.yadrotestovoe.data.contact.local.model

// Модель data-слоя
data class ContactLocal(
    val id: Long,
    val name: String?,
    val phoneNumber: String?,
    val phoneType: Int?,
    val thumbnailUri: String?, // Иконка пользователя
)
