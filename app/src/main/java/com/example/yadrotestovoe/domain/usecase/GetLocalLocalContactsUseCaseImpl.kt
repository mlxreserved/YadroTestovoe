package com.example.yadrotestovoe.domain.usecase

import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import javax.inject.Inject

class GetLocalLocalContactsUseCaseImpl @Inject constructor(
    private val contactLocalRepository: ContactLocalRepository
) : GetLocalContactsUseCase {

    override operator fun invoke(): List<Contact> = contactLocalRepository.getContacts()

}