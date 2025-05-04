package com.example.yadrotestovoe.domain.usecase.impl

import com.example.yadrotestovoe.domain.repository.AidlRepository
import com.example.yadrotestovoe.domain.usecase.inter.UnbindServiceUseCase
import javax.inject.Inject

class UnbindServiceUseCaseImpl @Inject constructor(private val aidlRepository: AidlRepository) : UnbindServiceUseCase {

    override operator fun invoke() = aidlRepository.unbindService()

}