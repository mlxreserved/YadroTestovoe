package com.example.yadrotestovoe.data.contact.local.source

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.yadrotestovoe.data.contact.local.model.ContactLocal
import com.example.yadrotestovoe.data.contact.local.util.Projections

class ContactContentDataSource(private val contentResolver: ContentResolver) :
    ContactLocalDataSource {

    override fun getLocalContacts(): List<ContactLocal> {
        val localContacts = mutableListOf<ContactLocal>()

        // Курсор для обращения к таблице с ID, именем и путем до фото пользователя
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            ),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val contactId = it.getLong(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val primaryPhoneNumber =
                    getPrimaryNumberFromContacts(contentResolver, contactId.toString())
                val thumbnailUri =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))

                localContacts.add(
                    ContactLocal(
                        id = contactId,
                        name = name,
                        phoneNumber = primaryPhoneNumber,
                        thumbnailUri = thumbnailUri
                    )
                )
            }
        }

        return localContacts
    }


    // Функция для получения основного номера, который помечен Default
    private fun getPrimaryNumberFromContacts(
        contentResolver: ContentResolver,
        contactId: String
    ): String? {

        // Курсор для обращения к таблице с номерами телефонов
        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            Projections.Number,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        phoneCursor?.use { cursor ->
            var fallbackNumber: String? = null
            while (cursor.moveToNext()) {
                val number = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                val isPrimary = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.IS_PRIMARY)
                )
                if (isPrimary > 0) {
                    return number  // нашли основной номер — возвращаем сразу
                }
                if (fallbackNumber == null) {
                    fallbackNumber = number  // запомним первый найденный номер
                }
            }
            return fallbackNumber  // если основного не нашли, вернём первый попавшийся
        }
        return null // Если нет ни одного номера, то возвращаем Null
    }


}