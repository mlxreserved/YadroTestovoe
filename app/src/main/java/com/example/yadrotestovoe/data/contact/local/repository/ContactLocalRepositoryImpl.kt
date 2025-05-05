package com.example.yadrotestovoe.data.contact.local.repository

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.example.yadrotestovoe.data.contact.local.mapper.mapContactLocalToContact
import com.example.yadrotestovoe.data.contact.local.model.ContactLocal
import com.example.yadrotestovoe.data.contact.local.util.Projections
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactLocalRepositoryImpl(private val context: Context) :
    ContactLocalRepository {

    private val contentResolver = context.contentResolver

    // Основная функция для получения контактов
    override suspend fun getLocalContacts(): List<Contact> = withContext(Dispatchers.IO) {
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
                /*
                    Преобразование типа номера в читабельный вид, например:
                    1 - Домашний
                    2 - Мобильный
                    3 - Рабочий
                    и т.д.
                 */
                val typeLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                    context.resources,
                    typePhoneNumber ?: 0,
                    ""
                ).toString()



                localContacts.add(
                    ContactLocal(
                        id = contactId,
                        name = name,
                        phoneNumber = primaryPhoneNumber,
                        phoneType = typeLabel,
                        thumbnailUri = thumbnailUri
                    )
                )
            }
        }

        localContacts.map { localContact -> mapContactLocalToContact(localContact) }
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
                // Номер телефона
                val number = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                // Является основным или нет
                val isPrimary = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.IS_PRIMARY)
                )
                // Тип номера
                val typeInt = cursor.getInt(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE)
                )
                if (isPrimary > 0) {
                    return Pair(number, typeInt) // нашли основной номер — возвращаем сразу
                }
                if (fallbackNumber == null) {
                    fallbackNumber = number  // запомним первый найденный номер
                    fallbackType = typeInt // запомним тип первого найденного номера
                }
            }
            return Pair(
                fallbackNumber,
                fallbackType
            )  // если основной не нашли, вернём первый попавшийся
        }
        return Pair(null, null) // Если нет ни одного номера, то возвращаем Null
    }

}