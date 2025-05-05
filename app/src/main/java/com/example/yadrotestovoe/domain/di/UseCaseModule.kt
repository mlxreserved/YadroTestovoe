package com.example.yadrotestovoe.domain.di

import com.example.yadrotestovoe.domain.usecase.GetGroupedContactsUseCase
import com.example.yadrotestovoe.domain.usecase.GetGroupedContactsInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetGroupedContacts(
        interactor: GetGroupedContactsInteractor
    ): GetGroupedContactsUseCase

}