package com.example.yadrotestovoe.domain.usecase

import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import javax.inject.Inject

class GetGroupedContactsInteractor @Inject constructor(
    private val contactLocalRepository: ContactLocalRepository
) : GetGroupedContactsUseCase {

    override suspend fun getGroupedContacts(): Map<Char, List<Contact>> {

        val localContacts = contactLocalRepository.getLocalContacts()

        val groupedContacts: Map<Char, List<Contact>> = localContacts
            .groupBy { it.name?.firstOrNull()?.uppercaseChar() ?: SIGN_UNNAMED_CONTACT }
            .toSortedMap()

        return groupedContacts
    }

    companion object {
        // Константа знака для контактов, у которых нет имени
        private const val SIGN_UNNAMED_CONTACT = '#'
    }

}