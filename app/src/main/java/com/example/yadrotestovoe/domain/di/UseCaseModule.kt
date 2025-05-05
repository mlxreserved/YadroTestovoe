package com.example.yadrotestovoe.domain.di

import com.example.yadrotestovoe.domain.usecase.impl.BindServiceUseCaseImpl
import com.example.yadrotestovoe.domain.usecase.impl.DeleteDuplicateContactsUseCaseImpl
import com.example.yadrotestovoe.domain.usecase.inter.GetLocalContactsUseCase
import com.example.yadrotestovoe.domain.usecase.impl.GetLocalContactsUseCaseImpl
import com.example.yadrotestovoe.domain.usecase.impl.UnbindServiceUseCaseImpl
import com.example.yadrotestovoe.domain.usecase.inter.BindServiceUseCase
import com.example.yadrotestovoe.domain.usecase.inter.DeleteDuplicateContactsUseCase
import com.example.yadrotestovoe.domain.usecase.inter.UnbindServiceUseCase
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
    abstract fun bindGetLocalContacts(
        impl: GetLocalContactsUseCaseImpl
    ): GetLocalContactsUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteDuplicateContactsUseCase(
        impl: DeleteDuplicateContactsUseCaseImpl
    ): DeleteDuplicateContactsUseCase

    @Binds
    @Singleton
    abstract fun bindBindServiceUseCase(
        impl: BindServiceUseCaseImpl
    ): BindServiceUseCase

    @Binds
    @Singleton
    abstract fun bindUnbindServiceUseCase(
        impl: UnbindServiceUseCaseImpl
    ): UnbindServiceUseCase

}