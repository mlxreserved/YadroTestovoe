package com.example.yadrotestovoe.domain.repository

import com.example.yadrotestovoe.domain.model.Contact

interface ContactLocalRepository {

    // Получение локальных контактов
    suspend fun getLocalContacts() : List<Contact>

}