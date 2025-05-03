package com.example.yadrotestovoe.domain.usecase

import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import javax.inject.Inject

class GetLocalContactsUseCaseImpl @Inject constructor(
    private val contactLocalRepository: ContactLocalRepository
) : GetLocalContactsUseCase {

    override suspend operator fun invoke(): List<Contact> = contactLocalRepository.getContacts()

}