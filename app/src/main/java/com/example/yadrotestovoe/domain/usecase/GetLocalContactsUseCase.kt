package com.example.yadrotestovoe.domain.usecase

import com.example.yadrotestovoe.domain.model.Contact

interface GetLocalContactsUseCase {

    suspend operator fun invoke(): List<Contact>

}