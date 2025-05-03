package com.example.yadrotestovoe.data.contact.local.model

/*
    Модель data-слоя
    ID - идентификационный номер контакта
    name - имя контакта (имя и фамилия вместе, если есть)
    phoneNumber - номер телефона
    thumbnailUri - путь к иконке пользователя
    phoneType - тип номера телефона
*/
data class ContactLocal(
    val id: Long,
    val name: String?,
    val phoneNumber: String?,
    val phoneType: Int?,
    val thumbnailUri: String?, // Иконка пользователя
)
