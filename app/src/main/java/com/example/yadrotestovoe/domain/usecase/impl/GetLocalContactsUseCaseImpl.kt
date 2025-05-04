package com.example.yadrotestovoe.domain.usecase.impl

import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import com.example.yadrotestovoe.domain.usecase.inter.GetLocalContactsUseCase
import javax.inject.Inject

class GetLocalContactsUseCaseImpl @Inject constructor(
    private val contactLocalRepository: ContactLocalRepository
) : GetLocalContactsUseCase {

    override suspend operator fun invoke(): List<Contact> = contactLocalRepository.getContacts()

}