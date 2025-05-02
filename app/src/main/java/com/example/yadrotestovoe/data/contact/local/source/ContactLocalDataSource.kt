package com.example.yadrotestovoe.data.contact.local.source

import com.example.yadrotestovoe.data.contact.local.model.ContactLocal

interface ContactLocalDataSource {

    // Получение локальных контактов
    fun getLocalContacts(): List<ContactLocal>

}