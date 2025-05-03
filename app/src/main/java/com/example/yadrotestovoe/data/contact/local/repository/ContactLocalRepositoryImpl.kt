package com.example.yadrotestovoe.data.contact.local.repository

import com.example.yadrotestovoe.data.contact.local.mapper.mapContactLocalToContact
import com.example.yadrotestovoe.data.contact.local.source.ContactLocalDataSource
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository

class ContactLocalRepositoryImpl(private val dataSource: ContactLocalDataSource) :
    ContactLocalRepository {

    // Получаем локальные контакты и преобразуем их в domain-модель
    override suspend fun getContacts(): List<Contact> = dataSource.getLocalContacts().map { contactLocal ->
        mapContactLocalToContact(contactLocal)
    }


}