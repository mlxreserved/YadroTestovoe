package com.example.yadrotestovoe.domain.usecase

import com.example.yadrotestovoe.domain.model.Contact

interface GetLocalContactsUseCase {

    operator fun invoke(): List<Contact>

}