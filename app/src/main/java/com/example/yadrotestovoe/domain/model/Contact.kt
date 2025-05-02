package com.example.yadrotestovoe.domain.model

data class Contact (
    val id: Long,
    val name: String,
    val phoneNumber: String?,
    val thumbnailUri: String?,
)