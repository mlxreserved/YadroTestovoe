package com.example.yadrotestovoe.data.contact.local.di

import android.content.Context
import com.example.yadrotestovoe.data.contact.local.repository.ContactLocalRepositoryImpl
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactModule {

    @Provides
    @Singleton
    fun provideContactLocalRepository(@ApplicationContext context: Context): ContactLocalRepository =
        ContactLocalRepositoryImpl(context)

}