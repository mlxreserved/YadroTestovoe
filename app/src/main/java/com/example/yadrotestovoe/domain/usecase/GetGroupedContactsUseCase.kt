package com.example.yadrotestovoe.domain.usecase

import com.example.yadrotestovoe.domain.model.Contact

interface GetGroupedContactsUseCase {

    // Выполнение use case для получения контактов
    suspend fun getGroupedContacts(): Map<Char, List<Contact>>

}