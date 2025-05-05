package com.example.app_server.model

data class ContactModel(
    val id: Long,
    val name: String?,
    val phoneNumbers: List<Phone>?,
    val emails: List<String>?,
    val thumbnailUri: String?,
    val lookupKey: String?,
    val company: String?
)

data class Phone(
    val number: String,
    val type: Int  // Тип номера, например, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
)