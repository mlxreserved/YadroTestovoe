package com.example.yadrotestovoe.data.contact.local.di

import android.content.ContentResolver
import android.content.Context
import com.example.yadrotestovoe.data.contact.local.repository.ContactLocalRepositoryImpl
import com.example.yadrotestovoe.data.contact.local.source.ContactContentDataSource
import com.example.yadrotestovoe.data.contact.local.source.ContactLocalDataSource
import com.example.yadrotestovoe.domain.repository.ContactLocalRepository
import dagger.Binds
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
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    @Singleton
    fun provideContactLocalDataSource(contentResolver: ContentResolver): ContactLocalDataSource =
        ContactContentDataSource(contentResolver)

    @Provides
    @Singleton
    fun provideContactLocalRepository(contactLocalDataSource: ContactLocalDataSource): ContactLocalRepository =
        ContactLocalRepositoryImpl(contactLocalDataSource)

}