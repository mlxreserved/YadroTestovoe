package com.example.yadrotestovoe.domain.usecase.impl

import com.example.yadrotestovoe.domain.repository.AidlRepository
import com.example.yadrotestovoe.domain.usecase.inter.BindServiceUseCase
import javax.inject.Inject

class BindServiceUseCaseImpl @Inject constructor(private val aidlRepository: AidlRepository) :
    BindServiceUseCase {

    override suspend operator fun invoke() = aidlRepository.bindService()

}