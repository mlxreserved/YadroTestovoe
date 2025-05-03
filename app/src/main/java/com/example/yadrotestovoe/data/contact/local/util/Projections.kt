package com.example.yadrotestovoe.data.contact.local.util

import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds

object Projections {

    // Список аргументов, которые необходимо получить из таблицы для номера
    val Number = arrayOf(
        CommonDataKinds.Phone.NUMBER,
        CommonDataKinds.Phone.IS_PRIMARY,
        CommonDataKinds.Phone.TYPE
    )

    // Список аргументов, которые необходимо получить из таблицы для контакта
    val ContactInfo = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
    )
}