package com.example.yadrotestovoe.domain.usecase.inter

interface DeleteDuplicateContactsUseCase {

    suspend operator fun invoke(): Result<String>

}