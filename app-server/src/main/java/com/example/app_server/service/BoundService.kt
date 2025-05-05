package com.example.app_server.service

import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.ContactsContract
import com.example.aidl.AsyncCallback
import com.example.aidl.CustomAidlException
import com.example.aidl.DeleteDuplicateContacts
import com.example.app_server.model.Phone
import com.example.app_server.model.ContactModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoundService : Service() {


    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? {
        return object : DeleteDuplicateContacts.Stub() {
            override fun deleteDuplicateContacts(callback: AsyncCallback?) {
                serviceScope.launch {
                    try {
                        val result = performDeleteDuplicates()
                        // Вернуться в main-поток, чтобы безопасно вызвать callback
                        withContext(Dispatchers.Main) {
                            if(result == 0){
                                callback?.onEmpty(result)
                            }
                            callback?.onSuccess(result)
                        }
                    } catch (e: Throwable) {
                        if (e is RuntimeException) {
                            val customAidlException = CustomAidlException(e.message)
                            callback?.onError(customAidlException)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // Отменяем все запущенные корутины при уничтожении сервиса
    }

    // Симуляция тяжёлой операции
    private fun performDeleteDuplicates(): Int {
        val contentResolver = applicationContext.contentResolver
        val deletedCount = mutableListOf<Long>()

        val contacts = loadAllContacts(contentResolver)

        // Группируем по ключу (все поля + количество непустых)
        val grouped = contacts.groupBy { contact ->
            val nonEmptyCount = listOfNotNull(
                contact.name?.takeIf { it.isNotBlank() },
                contact.phoneNumbers?.takeIf { it.isNotEmpty() },
                contact.thumbnailUri?.takeIf { it.isNotBlank() },
                contact.emails?.takeIf { it.isNotEmpty() },
                contact.company?.takeIf { it.isNotBlank() }
            ).size
            // Собираем номера и типы, сортируем по типу и номеру
            val phoneNumbersWithTypes = contact.phoneNumbers?.sortedWith(compareBy({ it.type }, { it.number }))
                ?.joinToString(separator = "|") { phone ->
                    "${phone.number}|${phone.type}"
                } ?: ""

            val sortedEmails = contact.emails?.sorted()?.joinToString("|") ?: 0

            // Создаём ключ с учётом всех данных контакта и типов номеров
            "${contact.name}|$phoneNumbersWithTypes|$sortedEmails|${contact.thumbnailUri}|$nonEmptyCount|${contact.company}"
        }

        grouped.forEach { (_, duplicates) ->
            if (duplicates.size > 1) {
                // Оставляем первый, остальные удаляем
                duplicates.drop(1).forEach { duplicate ->
                    val lookupUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        duplicate.lookupKey
                    )
                    val rows = contentResolver.delete(lookupUri, null, null)
                    if (rows > 0) {
                        deletedCount.add(duplicate.id)
                    }
                }
            }
        }

        return deletedCount.size
    }

    private fun loadAllContacts(contentResolver: ContentResolver): List<ContactModel> {
        val contacts = mutableListOf<ContactModel>()

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
            ),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val lookupKey = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val thumbnailUri = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                val company = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY))
                val phoneNumbers = mutableListOf<Phone>()
                val emails = mutableListOf<String>()

                // Получаем все номера телефонов
                val hasPhone = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhone > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        arrayOf(
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE
                        ),
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(id.toString()),
                        null
                    )
                    phoneCursor?.use { pCursor ->
                        while (pCursor.moveToNext()) {
                            val number = pCursor.getString(pCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val type = pCursor.getInt(pCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE))
                            phoneNumbers.add(Phone(number, type))
                        }
                    }
                }

                // Получаем все email-адреса
                val emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                    "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
                    arrayOf(id.toString()),
                    null
                )
                emailCursor?.use { eCursor ->
                    while (eCursor.moveToNext()) {
                        val email = eCursor.getString(eCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))
                        emails.add(email)
                    }
                }

                contacts.add(
                    ContactModel(
                        id = id,
                        lookupKey = lookupKey,
                        name = name,
                        phoneNumbers = phoneNumbers,
                        emails = emails,
                        thumbnailUri = thumbnailUri,
                        company = company
                    )
                )
            }
        }

        return contacts
    }
}