package com.example.yadrotestovoe.data.contact.local.source

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.yadrotestovoe.data.contact.local.model.ContactLocal
import com.example.yadrotestovoe.data.contact.local.util.Projections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactContentDataSource(private val contentResolver: ContentResolver) :
    ContactLocalDataSource {

    override suspend  fun getLocalContacts(): List<ContactLocal> = withContext(Dispatchers.IO){
        val localContacts = mutableListOf<ContactLocal>()

        // Курсор для обращения к таблице с ID, именем и путем до фото пользователя
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            Projections.ContactInfo,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val contactId = it.getLong(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val (primaryPhoneNumber, typePhoneNumber) =
                    getPrimaryNumberAndTypeFromContacts(contentResolver, contactId.toString())
                val thumbnailUri =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))


                localContacts.add(
                    ContactLocal(
                        id = contactId,
                        name = name,
                        phoneNumber = primaryPhoneNumber,
                        thumbnailUri = thumbnailUri,
                        phoneType = typePhoneNumber
                    )
                )
            }
        }

        localContacts
    }


    // Функция для получения основного номера, который помечен Default
    private fun getPrimaryNumberAndTypeFromContacts(
        contentResolver: ContentResolver,
        contactId: String
    ): Pair<String?, Int?> {

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
            var fallbackType: Int? = null
            while (cursor.moveToNext()) {
                val number = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                val isPrimary = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.IS_PRIMARY)
                )
                val typeInt = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE)
                )
                if (isPrimary > 0) {
                    return Pair(number, typeInt) // нашли основной номер — возвращаем сразу
                }
                if (fallbackNumber == null) {
                    fallbackNumber = number  // запомним первый найденный номер
                    fallbackType = typeInt
                }
            }
            return Pair(fallbackNumber, fallbackType)  // если основного не нашли, вернём первый попавшийся
        }
        return Pair(null, null) // Если нет ни одного номера, то возвращаем Null
    }


}