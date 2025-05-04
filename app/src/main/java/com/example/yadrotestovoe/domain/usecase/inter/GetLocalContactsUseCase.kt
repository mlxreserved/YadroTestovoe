package com.example.yadrotestovoe.domain.usecase.inter

import com.example.yadrotestovoe.domain.model.Contact

interface GetLocalContactsUseCase {

    // Выполнение use case для получения контактов
    suspend operator fun invoke(): List<Contact>

}