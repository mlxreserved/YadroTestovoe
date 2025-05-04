package com.example.yadrotestovoe.domain.usecase.impl

import com.example.yadrotestovoe.domain.repository.AidlRepository
import com.example.yadrotestovoe.domain.usecase.inter.DeleteDuplicateContactsUseCase
import javax.inject.Inject

class DeleteDuplicateContactsUseCaseImpl @Inject constructor(private val aidlRepository: AidlRepository) :
    DeleteDuplicateContactsUseCase {

    override suspend operator fun invoke(): Result<String> = aidlRepository.deleteDuplicateContacts()

}