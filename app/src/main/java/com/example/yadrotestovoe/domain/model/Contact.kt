package com.example.yadrotestovoe.domain.model


/*
    Модель domain-слоя
    ID - идентификационный номер контакта
    name - имя контакта (имя и фамилия вместе, если есть)
    phoneNumber - номер телефона
    thumbnailUri - путь к иконке пользователя
    phoneType - тип номера телефона
*/
data class Contact (
    val id: Long,
    val name: String?,
    val phoneNumber: String?,
    val thumbnailUri: String?,
    val phoneType: String?
)